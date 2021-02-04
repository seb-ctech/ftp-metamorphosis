(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system :as f]
              [metamorphosis.meta-ruleset.formal-system-example-structure :as example]
              [metamorphosis.meta-ruleset.translation :as meta-t]))

;TODO: A sequence of commands that are needed to make the evolution to the next level possible:
; an additional helper class, that translates to a set of instructions, that make the transition to a higher scope possible 
;and some scaling/translation for graphic implementation
(defn glue []
    (:sequence (f/build-random-axiom 2)))

;TODO: Implement a few functions that operate on sequences. 
;Unit: creates something new, Transform: Changes the sequence, Property: applies some overall change to the structure
(def fs->mutation {
    :unit [
        (list (fn [n sequence] (identity sequence)) ['(1)]) ; [ () ] TODO: add vectors of argument lists
    ]

    :transform [
        (list (fn [n sequence] (identity sequence)) ['(2)])
    ]

    :property [
        (list (fn [ sequence] (identity sequence)))
    ]
})

(defn pass-down [lower-scope]
    )

;TODO: Implement an algorithm that separates formal system into branches depending on itself.

;TODO: Instructions to arrange the structure of the lower level in the current level:
; is this also decided by the formal-system?

(defn change-letter [letter]
    (f/random-entry))

(defn has-lower-level? [entry]
    (and (contains? entry :gen)))

(defn higher-order-> []
    (:sequence (f/build-random-axiom 2)))

(defn mutate 
    [structure rate]
    (loop [sequence (:sequence structure) 
           new-sequence [] ]
        (if (> (count sequence) 0)
            (let [next (first sequence)
                  remaining (rest sequence)]
                (if (has-lower-level? next)
                    (recur remaining (conj new-sequence (mutate next rate)))
                    (if (< (rand) rate)
                        (case (rand-int 3)
                            0 (recur remaining new-sequence) ;Remove entry
                            1 (recur remaining (conj (conj new-sequence next) next)) ;Dublicate entry
                            2 (recur remaining (conj new-sequence (change-letter next)))) ;Change entry
                        (recur remaining (conj new-sequence next)))))
            (assoc structure :sequence new-sequence))))
 
;TODO: Compose mutations needs to be repeated on lower levels
; for example: Phrase 2 of Sentence 1 has two repetitions of motif and Phrase 1 of Sentence 2 has four repetitions of motif
            
(defn compose-mutations [structure]
    (let [times (inc (rand-int 3))
          variation (higher-order->)]
        (loop [remaining times composition []]
            (if (> remaining 0)
                (recur 
                    (dec remaining)
                    (conj 
                        (reduce conj composition variation) 
                        (mutate structure 0.3)))
                composition))))  


(defn compose-mutation-sequence
    "Function that makes a composed function out of a partial sequence and that takes the same sequence as input" 
    [part-sequence]
    (let [linear-list (meta-t/fs-sequence->instructions part-sequence fs->mutation)]
        ((apply comp (map #(if (seq? %) 
                    (apply partial %) 
                    %) linear-list)) part-sequence)))

;TODO: mutate/compose-mutation must implement a macro that get's composed out of the formal system aswell
; to create different kinds of evolution/mutations on single entries or entire "levels"
; must operate with sequence and collection functions.

(defn build-mutation-algorithm [sequence]
    )

; Similar to graphical translation, but instead of list in it is a nested list "(transform (property (unit 433) 23) 23)" for one scope
(defn meta-mutate [structure]
    (eval (build-mutation-algorithm structure)))


(defn test-mutate-simple []
    (compose-mutation-sequence (:sequence example/simple)))


(defn test-mutate-complex []
    (meta-mutate example/complex))