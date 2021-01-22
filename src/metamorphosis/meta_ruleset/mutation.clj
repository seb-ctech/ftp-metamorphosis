(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system :as f]))

;TODO: A sequence of commands that are needed to make the evolution to the next level possible:
; some recursive property for mutation macro 
;and some scaling/translation for graphic implementation
(defn glue []
    (:sequence (f/build-random-axiom 2)))

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

;TODO: mutate/compose-mutation must implement a macro that get's composed out of the formal system aswell
; to create different kinds of evolution/mutations on single entries or entire "levels"
; must operate with sequence and collection functions.

(defn build-mutation-algorithm [sequence]
    )

(defn meta-mutate [structure]
    (eval (build-mutation-algorithm structure)))