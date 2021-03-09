(ns metamorphosis.meta-ruleset.translation
    (:require [metamorphosis.event-listener.input.core :as in]))

(def input-signals in/input-signals)

(defn get-entry-position 
    "A function that returns the index of a given entry within a sequence"
    [sequence entry]
    (first 
        (first 
            (filter #(= (second %) entry) 
                    (map-indexed vector sequence)))))

(defn cap-entry-amount
    "A function that given an instruction list caps the index at the max and the min of that list length"
    [params amount]
    (if (and (vector? params) (> (count params) 0))
        (let [max-index (dec (count params))
              capped-index (cond (< amount 0)
                                    (max 0 amount)
                                 (> amount max-index)
                                    (min amount max-index)
                                :else amount)]
            (get params capped-index))
        (println (str "(Check instruction list) Not a vector or vector is empty: " params))))

(defn wrap-entry-index 
    "A function that returns the next possible index of a sequence, 
    by wrapping around the length of the sequence. Also works with negative numbers"
    [vec index]
    (let [length (count vec)]
            (if (and (vector? vec) (> length 0))
                (get vec (mod index length))
                (println (str "(Check instruction list) Not a vector or vector is empty: " vec)))))

(defn formal-system->form 
    "A function that serves as abstraction and can translate an entry 
    from the formal system into a provided instruction set"
    [entry translation-unit amount]
    (let [{class :class
          index :index} entry
          amount (:index amount)
          instruction (if (= class :glue) 
                          (first (translation-unit class))
                          (wrap-entry-index (translation-unit class) index))
          params (when (second instruction) (cap-entry-amount (second instruction) amount))]
          (if (= class :glue)
              (cons (first instruction) (:values entry))
              (if params
                (cons (first instruction) params)
                (first instruction)))))

(defn fs-sequence->instructions
    "A function that transforms a sequence of the formal system to valid provided instruction-set"
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

(defn evaluate-break 
    "A function that returns true when the signal is not a break or a break that is longer than a threshhold.
    Used to clean noisy input"
    [signal]
    (let [threshhold 0.001]
        (or (not= (:signal signal) :break)
            (> (float (:duration signal)) threshhold))))

(defn translate-input-signal 
    "A function that translates a single signal from the input sequence to command class and index"
    [input index unit?]
        (let [amount (int (* (float (:duration input)) 10 3))]
            (conj
                (vector {:class :amount :index amount})
                {:class (if unit?
                            :unit 
                            (if (odd? index) 
                                :property
                                :transform))
                :index (get-entry-position input-signals (:signal input))})))

(defn process-input
    "A function that takes an abstract input sequence 
    and transforms it into an initital axiom for the meta-formal-system"
    [input] 
    (let [input-seq (filter evaluate-break input)]
        (loop [group []
               remaining input-seq
               final-sequence []]
            (let [next (first remaining)]
                (if (> (count remaining) 0)
                    (if (or (= (:signal next) :break) (= (count (rest remaining)) 0))
                        (recur [] 
                                (rest remaining) 
                                (into final-sequence 
                                    (flatten (map-indexed (fn [index, entry] 
                                                    (translate-input-signal 
                                                        entry 
                                                        index 
                                                        (= (dec (count group)) index)))
                                                group))))
                        (recur (conj group next) 
                               (rest remaining) 
                                final-sequence))
                    final-sequence)))))