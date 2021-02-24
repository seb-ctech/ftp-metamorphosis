(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system :as f]
              [metamorphosis.meta-ruleset.formal-system-example-structure :as example]
              [metamorphosis.meta-ruleset.translation :as meta-t]))

(defn glue 
    "A function that returns an addition helper class that behaves as a fix and is aware of all copies"
    ([n]
     {:class :glue :values (list (inc n))})
    ([n i]
     {:class :glue :values (list (inc n) i)}))

(defn has-lower-level? [entry]
    "A function that returns true of false, wether an entry has a lower-level sequence"
    (and (contains? entry :gen)))

(defn meta-rate 
    "Function that given a sequence calculates the mutation rate 
    of the lower scope based on a ratio between transform and property indeces"
    [sequence]
    (let [transforms (inc (apply + (map #(:index %) 
                                        (filter #(= (:class %) :transform) sequence))))
          properties (inc (apply + (map #(:index %) 
                                        (filter #(= (:class %) :property) sequence))))]
    (float (/ (min transforms properties) (max transforms properties)))))

; These functions have n arguments for arbitrary parametrization, a mutation rate and the sequence as input.
; Unit: creates something new, 
;Transform: Changes the sequence, 
; TODO: Property: applies some overall change to the next units (lower level sequences)
; TODO: eliminate parameters. Just use Rate. 

(defmacro ignore-glue [entry form]
    "A macro that can be used to make an if form as wrapper to return :glue unmodified"
    `(if (= (:class ~entry) :glue) ~entry ~form))

(def fs->mutation {
    :glue [
        (list
            (fn 
                ([n rate sequence]
                sequence)
                ([n x rate sequence]
                    sequence)))
    ]
    :unit [
        ; Drop entry at target index
        (list 
            (fn [n rate sequence] 
                (into (vector) 
                      (map #(second %) 
                            (filter #(not(= n (first %)))
                                     (map-indexed vector sequence))))) 
            ['(0) '(1) '(2) '(3) '(4)])
        ; Duplicate entry at target index
        (list 
            (fn [n rate sequence] 
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
        ; Swap two entries at target index
        (list 
            (fn [n rate sequence]
                (let [first-cut (split-at 
                                (dec n) 
                                (partition 2 sequence))
                      before (first first-cut)
                      middle (first (split-at 2 (second first-cut)))
                      after (second (split-at 2 (second first-cut)))]
                (into (vector) (flatten (list before (reverse middle) after)))))
            ['(0) '(1) '(2) '(3) '(4)])
    ]

    :property [
        ; Decrement all by 1
        (list 
            (fn [rate sequence]
                (map #(ignore-glue 
                        %
                        (if (has-lower-level? %)
                            %
                            (assoc % :index
                                (let [i (:index %)] 
                                    (if (> i 0)
                                        (dec i)
                                        i)))))
                     sequence)))
        ; Increment all by 1
        (list 
            (fn [rate sequence]
                (map #(ignore-glue 
                            % 
                            (if (has-lower-level? %)
                                %
                                (assoc % :index
                                    (let [i (:index %)]
                                        (if (< i 5)
                                            (inc i)
                                            i)))))
                     sequence)))
    ]
})

;TODO: Implement
(defn repetitions [structure]
    "Function that uses a deterministic algorithm to determine an amount between 1 and 7 for the next step"
    (inc (rand-int 3)))

;TODO: Make deterministic
(defn higher-order-> [theorem]
    "Function that computes a sequence of one to three random classes by some deterministic algorithm"
    (:sequence (f/build-random-axiom 2)))
 
; How do you avoid recursion in here? IMPORTART: To make the point between meta and recursion
; Solution: It must be ignored and not resolved! So I need to filter out lower levels on translation and keep it in when its passed as input
(defn meta-mutate-sequence
    "Function that makes a composed function out of 
    a partial sequence and that takes the same sequence as input and outputs a mutated version of it" 
    [part-sequence]
    ;(println "Meta Sequence: " part-sequence "\n")
    (let [linear-command-list (meta-t/fs-sequence->instructions 
                                    (filter #(not (has-lower-level? %)) 
                                            part-sequence) 
                                    fs->mutation)]
        ((apply 
            comp 
            (map 
                #(partial (if (seq? %) 
                              (apply partial %) 
                              %) (meta-rate part-sequence))
                linear-command-list))
        part-sequence)))

(defn recursive-system-mutation 
    "Function that takes a structure of :gen and :sequence 
    and recursively applies meta-mutate-sequence on lower-scope sequences"
    [structure]
    (let [sequence (:sequence structure)
          gen (:gen structure)]
    {:gen gen 
     :sequence (into 
                    (vector) 
                    (meta-mutate-sequence 
                        (map #(if (has-lower-level? %)
                                  (recursive-system-mutation %)
                                  %)
                            sequence)))}))

; Similar to graphical translation, but instead of list in it is a nested list "(transform (property (unit 433) 23) 23)" for one scope
(defn meta-mutate [structure]
    "Function that takes the previous generation's structure as input makes 
    several copies, then applies a recursive meta mutation on all copies and composes them together by fitting
    them in a higher order structure with a glue and some top-level variations"
    (let [times (repetitions structure)
          old structure]
        (loop [remaining times 
               composition (conj 
                                [(glue times)]
                                old)]
            (if (> remaining 0)
                (recur 
                    (dec remaining)
                    (conj 
                        (reduce 
                            conj 
                            composition 
                            (into (vector) (cons (glue times remaining) (higher-order-> structure)))) 
                        (recursive-system-mutation structure)))
                composition))))


; ======= TEST FUNCTIONS ===============================

(defn test-single-command [class index]
    (let [command (get (class fs->mutation) index)]
        (println (str "Before: " (:sequence example/simple)))
        (apply (first command) 
               (if (> (count command) 1) 
                   (list 2 0.2 (:sequence example/simple))  
                   (list 0.2 (:sequence example/simple))))))

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