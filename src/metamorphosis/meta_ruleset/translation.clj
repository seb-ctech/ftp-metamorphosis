(ns metamorphosis.meta-ruleset.translation
    (:require [metamorphosis.event-listener.input.core :as in]))

(def test-input in/test-input)
(def input-signals in/input-signals)


(defn strongest-signal [input]
    (reduce #(if (> (:intensity %2) (:intensity %1))
                    %2
                    %1)
        {:intensity 0}
        (filter #(contains? % :intensity) 
            input)))

(defn get-entry-position [sequence entry]
    (first (first (filter #(= (second %) entry) (map-indexed vector sequence)))) )

(defn translate-input-signal [input unit?]
     (let [amount (* (:duration input) 10)]
        (conj (conj [] (assoc {:class :amount} :index (Math/round (* amount (:intensity input)))))   
            (assoc {:class (if unit?
                                :unit 
                                (if (> (:intensity input) 0.5) 
                                    :transform
                                    :property))} 
                    :index (get-entry-position input-signals (:signal input))))))

(defn make-first-two [input]
    (let [strongest (strongest-signal input)
          length (count input)
          index-of-strongest (get-entry-position input strongest)]
        (conj (if (> index-of-strongest 0) 
                    (vector (get input (dec index-of-strongest)))
                    []) strongest)))

;TODO: How do you account for breaks? For now I keep it out
;TODO: Can I refactor this more elegantly?

;FIXME: Amount needs to be allocated properly for reverse...
(defn swap-amount [sequence]
    ;1. Divide Sequence in groups of two
    ;2. Map Over it with reverse
    ;3. Reassemble into one sequence
    sequence)

(defn process-input [input] 
    (let [input-sequence (into [] (filter #(not (= (:signal %) :break)) input))
          first-two (make-first-two input-sequence)
          rest (filter #(not (contains? (set first-two) %)) input-sequence)]
        (println first-two)
        (into (into [] (if (> (count first-two) 1)
                        (into (translate-input-signal (first first-two) false) 
                                (translate-input-signal (second first-two) true))
                        (translate-input-signal (first first-two) true)))
            (swap-amount (reverse (reduce #(into %1 
                                    (translate-input-signal (second %2) 
                                        (= (mod (first %2) 3) 0)))
                            []
                            (map-indexed vector (reverse rest))))))))