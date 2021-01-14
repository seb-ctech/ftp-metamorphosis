(ns metamorphosis.meta-ruleset
    (:gen-class))

;TODO: Define Alphabet
;TODO: Implement interfacing rules between L-System (String of Characters) and Graphic-Algorithms:
;      an abstract data structure which can be translated to a specific graphical implementation
;TODO: Implement function to create axiom from input-data-structure provided by the input-processor
;TODO: Implement meta-ruleset: that uses macros derived 
;      from the string of the current generation and when evaluated produces
;      the string for the next generation

(def axiom [:a :b :c :d])

(def alphabet ["A" "B" "C"])

(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 3) 2)))
    ([length]
        (loop [string []]
            (if (< (count string) length)
                (recur (conj string 
                            (get alphabet 
                                (rand-int (count alphabet)))))
                string))))

;TODO: A sequence of commands that are needed to make the evolution to the next level possible
(defn level-glue []
    (build-random-axiom 2))

;TODO: Instructions to arrange the structure of the lower level in the current level
(defn higher-order-> []
    (build-random-axiom 2))

(defn mutate 
    [structure]
    structure)

(defn next-step [structure]
    (println (str "Previous Generation:" " " structure))
    (let [times (inc (rand-int 3))
          mutated-continuation (reduce into
                                    []
                                    (repeat times 
                                        (into
                                            (list (mutate structure)) 
                                                (higher-order->))))]
        (reduce into (level-glue) [[structure] mutated-continuation])))

(defn parse-input 
    [input] input)


(defn motif->phrase []
    (next-step (build-random-axiom)))

(defn phrase->passage []
    (next-step (motif->phrase)))

(defn passage->movement []
    (next-step (phrase->passage)))

(defn motif->final []
    (next-step (passage->movement)))