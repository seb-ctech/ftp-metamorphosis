(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system :as f]
              [metamorphosis.meta-ruleset.formal-system-example-structure :as example]
              [metamorphosis.meta-ruleset.translation :as meta-t]))

;TODO: A sequence of commands that are needed to make the evolution to the next level possible:
; an additional helper class, that translates to a set of instructions, that make the transition to a higher scope possible 
;and some scaling/translation for graphic implementation
(defn glue []
    (:sequence (f/build-random-axiom 2))) ;TODO: Make deterministic

;TODO: Implement a few functions that operate on sequences.
; These functions have n arguments for arbitrary parametrization, a mutation rate and the sequence as input.
;Unit: creates something new, Transform: Changes the sequence, Property: applies some overall change to the structure
(def fs->mutation {
    :unit [
        (list (fn [n rate sequence] (identity sequence)) ['(0.1)])
    ]

    :transform [
        (list (fn [n rate sequence] (identity sequence)) ['(0.1)])
    ]

    :property [
        (list (fn [n rate sequence] (identity sequence)) ['(0.1)])
    ]
})

(defn change-letter [letter]
    (f/random-entry))

(defn has-lower-level? [entry]
    (and (contains? entry :gen)))

(defn higher-order-> []
    (:sequence (f/build-random-axiom 2))) ;TODO: Make deterministic
 
(defn meta-mutate-sequence
    "Function that makes a composed function out of a partial sequence and that takes the same sequence as input" 
    [rate part-sequence]
    (let [linear-list (meta-t/fs-sequence->instructions part-sequence fs->mutation)]
        ((apply comp (map 
                        #(partial (if (seq? %) 
                            (apply partial %) 
                            %) rate) 
                        linear-list)) part-sequence)))

(defn recursive-system-mutation 
    "Function that takes a structure of :gen and :sequence and recursively applies meta-mutate on lower-scope sequences"
    [structure]
    (let [sequence (:sequence structure)
          gen (:gen structure)]
    {:gen gen 
     :sequence (into (vector) (meta-mutate-sequence 0.3 (map #(if (has-lower-level? %)
                                        (recursive-sequence-mutation %)
                                        %)
        sequence)))}))

; Similar to graphical translation, but instead of list in it is a nested list "(transform (property (unit 433) 23) 23)" for one scope
; TODO: Make repetition deterministic
(defn meta-mutate [structure]
    (let [times (inc (rand-int 3))]
        (loop [remaining times composition []]
            (if (> remaining 0)
                (recur 
                    (dec remaining)
                    (conj 
                        (reduce conj composition (higher-order->)) 
                        (recursive-system-mutation structure)))
                composition))))

(defn test-mutate-simple []
    (meta-mutate example/simple))

(defn test-mutate-complex []
    (meta-mutate example/complex))