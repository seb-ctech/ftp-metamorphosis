(ns metamorphosis.meta-ruleset
    (:gen-class))

;TODO: Define Alphabet
;TODO: Implement interfacing rules between L-System (String of Characters) and Graphic-Algorithms:
;      an abstract data structure which can be translated to a specific graphical implementation
;TODO: Implement function to create axiom from input-data-structure provided by the input-processor
;TODO: Implement meta-ruleset: that uses macros derived 
;      from the string of the current generation and when evaluated produces
;      the string for the next generation

(def alphabet ["A" "B" "C"])

(defn random-alphabet-letter []
    (get alphabet (rand-int (count alphabet))))

(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 3) 2)))
    ([length]
        (loop [string []]
            (if (< (count string) length)
                (recur (conj string 
                            (random-alphabet-letter)))
                string))))

(defn parse-input 
    [input] input)

;TODO: A sequence of commands that are needed to make the evolution to the next level possible
(defn level-glue []
    (build-random-axiom 2))

;TODO: Instructions to arrange the structure of the lower level in the current level
(defn higher-order-> []
    (build-random-axiom 2))


(defn change-letter [letter]
    (random-alphabet-letter))

(defn has-lower-level? [entry]
    (vector? entry))

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

(defn next-step [structure]
    (println (str "Previous Generation:" " " structure))
    (reduce into 
        (level-glue) 
        [[structure] (compose-mutations structure)]))


;==== TEST FUNCTIONS ======

(defn motif->phrase []
    (next-step (build-random-axiom)))

(defn phrase->passage []
    (next-step (motif->phrase)))

(defn passage->movement []
    (next-step (phrase->passage)))

(defn motif->final []
    (next-step (passage->movement)))