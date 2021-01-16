(ns metamorphosis.meta-ruleset.mutation
    (:require [metamorphosis.meta-ruleset.formal-system :as f]))

;FIXME: Remove later. Only for developing purposes
(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 3) 2)))
    ([length]
        (loop [string []]
            (if (< (count string) length)
                (recur (conj string 
                            (f/random-alphabet-letter)))
                string))))

;TODO: A sequence of commands that are needed to make the evolution to the next level possible:
; some recursive property for mutation macro 
;and some scaling/translation for graphic implementation
(defn level-glue []
    (f/build-random-axiom 2))

;TODO: Instructions to arrange the structure of the lower level in the current level:
; is this also decided by the formal-system?

(defn change-letter [letter]
    (f/random-alphabet-letter))

(defn has-lower-level? [entry]
    (vector? entry))

(defn higher-order-> []
    (f/build-random-axiom 2))

(defn mutate 
    [structure rate]
    (loop [structure structure 
           new-structure [] ]
        (if (> (count structure) 0)
            (let [next (first structure)
                  remaining (rest structure)]
                (if (has-lower-level? next)
                    (recur remaining (conj new-structure (mutate next rate)))
                    (if (< (rand) rate)
                        (case (rand-int 3)
                            0 (recur remaining new-structure) ;Remove the letter
                            1 (recur remaining (conj (conj new-structure next) next)) ;Dublicate Letter
                            2 (recur remaining (conj new-structure (change-letter next)))) ;Change Letter
                        (recur remaining (conj new-structure next)))))
            new-structure)))
            
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

(defn meta-mutate [structure])