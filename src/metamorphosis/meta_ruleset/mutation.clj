(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system.core :as f]
              [metamorphosis.meta-ruleset.formal-system.examples :as example]
              [metamorphosis.meta-ruleset.translation :as meta-t]))

; These functions have n arguments for arbitrary parametrization, a mutation rate and the sequence as input.
; TODO: Unit: creates something new (can clone a sequence, just an entry, and cherry pick sequences from different levels) or remove
; TODO: Transform: Changes the relationship between the entries in the sequence, 
; Property: applies some overall change to the next units (lower level sequences)

(defmacro ignore-glue [entry form]
    "A macro that can be used to make an if form as wrapper to return :glue unmodified"
    `(if (= (:class ~entry) :glue) ~entry ~form))

(defn apply-property 
    "A wrapper function that applies a unit function to every unit in a sequence 
    and applies a sequence function to lower level sequences"
    [sequence unit-property sequence-property]
    (f/ready-> (map #(cond (f/has-lower-level? %) 
                            (assoc % :sequence (f/ready-> 
                                                    (sequence-property 
                                                        (f/transform-> (:sequence %)))))
                           (= (:class %) :unit) 
                            (unit-property %)
                           :else %) 
                    (f/transform-> sequence))))

(defn protect-sequence 
    "A function that applies a provided function only if the entry is not a sequence"
    [entry f]
    (if (f/has-lower-level? entry)
        entry
        (f entry)))

(def fs->mutation {
    :glue [
        (list
            (fn 
                ([n sequence]
                sequence)
                ([n x sequence]
                    sequence)))
    ]
    :unit [
        ; Drop entry at target index
        (list 
            (fn [n sequence] 
                (into (vector) 
                      (map #(second %) 
                            (filter #(not(= n (first %)))
                                     (map-indexed vector sequence))))) 
            ['(0) '(1) '(2) '(3) '(4)])
        ; Duplicate entry at target index
        (list 
            (fn [n sequence] 
                (into (vector)
                      (map #(second %) 
                            (reduce (fn [v entry]
                                        (if (not (= n (first entry)))
                                            (conj v entry)
                                            (conj (conj v entry) entry)))
                            []
                            (map-indexed vector sequence))))) 
            ['(0) '(1) '(2) '(3) '(4)])
    ]

    :transform [
        ; TODO: Swap two commands
        (list 
            (fn [n sequence]
                (let [first-cut (split-at 
                                (dec n) 
                                (partition 2 sequence))
                      before (first first-cut)
                      middle (first (split-at 2 (second first-cut)))
                      after (second (split-at 2 (second first-cut)))]
                (into (vector) (flatten (list before (reverse middle) after)))))
            ['(0) '(1) '(2) '(3) '(4)])
    ]
     ; TODO: Swap two amounts                         
    :property [
        ; Decrement all by 1
        (list 
            (fn [sequence]
                (let [unit-f #(do 
                                (when (nil? (:index %)) (println "Nil: " %))
                                (update % :index dec))
                      seq-f #(map (fn [entry] (protect-sequence entry unit-f))
                                  %)]
                (apply-property sequence unit-f seq-f))))
        ; Increment all by 1
        (list 
            (fn [sequence]
                    (let [unit-f #(do 
                                    (when (nil? (:index %)) (println "Nil: " %))
                                    (update % :index inc))
                          seq-f #(map (fn [entry] (protect-sequence entry unit-f))
                                      %)]
                    (apply-property sequence unit-f seq-f))))
    ]
})

(defn repetitions [structure]
    "Function that uses a deterministic algorithm to determine an amount between 1 and 7 for the next step"
    (let [units-count (count (:sequence structure))]
        (inc (mod (- units-count (:gen structure)) 6))))

(defn meta-rate
    "Function that given a sequence calculates the mutation rate 
    of the lower scope based on a ratio between transform and property indeces"
    [max copies remaining]
    (* max (/ (- copies (dec remaining)) copies)))

(defn higher-order-> [mutation index]
    "Function that computes a sequence of one to three command classes by some deterministic algorithm"
    (let [gen (:gen mutation)
          transformable (f/transform-> (:sequence mutation))
          units (filter #(f/command-class? :unit %) transformable)
          trans (filter #(f/command-class? :transform %) transformable)
          props (filter #(f/command-class? :property %) transformable)
          amounts (filter #(f/command-class? :amount %) transformable)
          commands (map f/read-command-pair (filter #(and (map? %) (not (f/has-lower-level? %))) 
                                                     (f/group-sequence transformable)))
          average-value (fn [class-list value] 
                            (let [count (count class-list)]
                                (if (> count 0)
                                    (int (/ (apply + (map value class-list)) count))
                                    0)))
          initial-sequence (into (vector) 
                                (f/build-command-pair
                                    :transform 
                                    (if (> (count units) 0)
                                        (:index (first units))
                                        (- (* index 2) 3))
                                    (int (+ (average-value amounts :index) 2 (int (/ index 2)) gen))))
          add-trans (if (< (count trans) 3)
                        (into initial-sequence 
                            (f/build-command-pair
                                :transform
                                (if (> (count trans) 0)
                                    (- 8 (- (:index (last trans)) (:index (first trans))))
                                    (int (/ (+ index 4) 
                                            (if (> (inc (- 3 index )) 0)
                                                (inc (- 3 index ))
                                                1))))
                                (int (- (* (average-value commands :amount) 2) 5 (int (/ index 2))))))
                        initial-sequence)
          add-prop (if (< (count props) 3)
                       (into add-trans 
                            (f/build-command-pair
                                :property
                                (reduce max 0 (filter #(not (map (fn [b] (= % b)) (map :index props))) (range 8)))
                                (int (+ (average-value props :index) 2))))
                        add-trans)
          add-unit (if (and (< (count units) 3) (> (count units) 0))
                       (into add-prop 
                            (f/build-command-pair
                                :unit
                                (if (> (count units) 0)
                                    (reduce min 100 (map :index units))
                                    (- 5 index))
                                (int (+ (average-value units :index) 2))))
                        add-prop)]
            (if  (f/command-class? :unit (last add-unit))
                 (into add-unit 
                    (f/build-command-pair
                        :transform
                        (if (> (count trans) 0) 
                            (inc (:index (last trans)))
                            (- index 2))
                        (+ (average-value commands :amount) 3)))
                 add-unit)))
        
 ;TODO: Make deterministic
(defn fuse-mutation 
    "A function that fuses an original sequence with a mutated one by a mutation rate"
    [original mutated rate]
    (let [original (f/group-sequence original)
          mutated (f/group-sequence mutated)
          length (max (count original) (count mutated))]
        (loop [index 0
               new-sequence []]
               (if (< index length)
                   (recur (inc index) (conj new-sequence (if (< (rand 1.0) rate)
                                                             (if (get mutated index) 
                                                                (get mutated index)
                                                                (get original index))
                                                             (if (get original index)
                                                                (get original index)
                                                                (get mutated index)))))
                    (f/ready-> (f/ungroup-sequence new-sequence))))))

; How do you avoid recursion in here? IMPORTART: To make the point between meta and recursion
; Solution: It must be ignored and not resolved! So I need to filter out lower levels on translation and keep it in when its passed as input
(defn meta-mutate-sequence
    "Function that makes a composed function out of 
    a partial sequence and that takes the same sequence as input and outputs a mutated version of it" 
    [part-sequence rate]
    (let [linear-command-list (meta-t/fs-sequence->instructions 
                                    (filter #(not (f/has-lower-level? %)) 
                                        part-sequence) 
                                        fs->mutation)]
        (fuse-mutation part-sequence 
            ((apply comp 
                    (map #(if (seq? %) 
                            (apply partial %) 
                            %) 
                        linear-command-list))
                part-sequence) rate)))

(defn recursive-system-mutation 
    "Function that takes a structure of :gen and :sequence 
    and recursively applies meta-mutate-sequence on lower-scope sequences"
    [structure rate]
    (let [sequence (:sequence structure)
          gen (:gen structure)
          copies (f/count-copies sequence)]
    (loop [new-sequence []
           remaining sequence
           index 0]
        (if (> (count remaining) 0)
            (let [next (first remaining)]
                (recur (conj new-sequence
                            (if (f/has-lower-level? next)
                                (recursive-system-mutation next (if (> copies 0)
                                                                    (meta-rate rate copies (- copies index))
                                                                    rate))
                                next))
                       (rest remaining)
                       (if (f/has-lower-level? next)
                           (inc index)
                           index)))
            {:gen gen :sequence (meta-mutate-sequence new-sequence rate)}))))

(defn meta-mutate [structure]
    "Function that takes the previous generation's structure as input makes 
    several copies, then applies a recursive meta mutation on all copies and composes them together by fitting
    them in a higher order structure with a glue and a higher-order prefix-sequence"
    (let [times (repetitions structure)
          original structure]
        (loop [remaining times 
               composition (vector original)]
            (if (> remaining 0)
                (recur 
                    (dec remaining)
                    (let [variation (recursive-system-mutation structure (meta-rate 1.0 times remaining))]
                        (conj 
                            (into composition 
                                  (higher-order-> variation remaining)) 
                            variation)))
                (f/ready-> composition)))))


; ======= TEST FUNCTIONS ===============================

(defn test-single-command 
    ([class index]
        (test-single-command class index (:sequence example/simple)))
    ([class index sequence]
    (let [command (get (class fs->mutation) index)]
        (println (str "Before: " sequence))
        (apply (first command) 
               (if (> (count command) 1) 
                   (list 2 0.2 sequence)  
                   (list 0.2 sequence))))))

(defn test-recursive-translation []
    (recursive-system-mutation example/nested))

(defn test-mutate-sequence []
    (println (str "Before: " (:sequence example/simple)))
    (meta-mutate-sequence 0.3 (:sequence example/simple)))

(defn test-mutate-simple []
    (println (str "Before: " example/simple))
    (println (str "<" (apply str (repeat 200 "-")) ">"))
    (meta-mutate example/simple))

(defn test-mutate-complex []
    (println (str "Before: " example/complex))
    (println (str "<" (apply str (repeat 200 "-")) ">"))
    (meta-mutate example/complex))