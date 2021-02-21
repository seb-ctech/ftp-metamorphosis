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

(defn next-possible-entry [vec index]
    (loop [current-index index]
        (let [entry (get vec current-index)]
            (if entry
                entry
                (if (>= current-index 0)
                    (recur (dec current-index))
                    (println (str "Not a vector or vector is empty: " vec)))))))

(defn formal-system->form 
    [entry translation-unit amount]
    (let [class (:class entry)
          index (:index entry)
          amount (:index amount)
          instruction (next-possible-entry (translation-unit class) index)
          params (when (second instruction) (next-possible-entry (second instruction) amount))]
          (if params
            (cons (first instruction) params)
            (first instruction))))


(defn fs-sequence->instructions
    "This is a function that transforms a sequence of the formal system to valid provided instruction-set"
    [sequence instruction-set]
    (loop [remaining sequence
            amount {:class :amount :index 0}
            instructions []]
        (if (> (count remaining) 0)
            (let [next (first remaining)]
                (if (= (:class next) :amount)
                    (recur (rest remaining) next instructions)
                    (recur (rest remaining) amount (conj instructions (formal-system->form next instruction-set amount)))))
            instructions)))

(defn make-first-two [input]
    (let [strongest (strongest-signal input)
          length (count input)
          index-of-strongest (get-entry-position input strongest)]
        (conj (if (> index-of-strongest 0) 
                    (vector (get input (dec index-of-strongest)))
                    []) strongest)))
                
;TODO: Find a better translation algorithm, that is not limited to 'every 3' pattern
;TODO: How do you account for breaks? For now I keep it out
;TODO: Can I refactor this more elegantly?

(defn swap-amount [sequence]
    (reduce #(conj %1 (first %2) (second %2))
        []
        (map reverse (partition 2 sequence))))

(defn process-input [input] 
    (let [input-sequence (into [] (filter #(not (= (:signal %) :break)) input))
          first-two (make-first-two input-sequence)
          rest (filter #(not (contains? (set first-two) %)) input-sequence)]
        (into (into [] (if (> (count first-two) 1)
                        (into (translate-input-signal (first first-two) false) 
                                (translate-input-signal (second first-two) true))
                        (translate-input-signal (first first-two) true)))
            (swap-amount (reverse (reduce #(into %1 
                                    (translate-input-signal (second %2) 
                                        (= (mod (first %2) 3) 0)))
                            []
                            (map-indexed vector (reverse rest))))))))