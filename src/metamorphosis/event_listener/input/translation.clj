(ns metamorphosis.event-listener.input.translation)

(def test-input [
    {:signal :A :intensity 0.1 :duration 1/10}
    {:signal :B :intensity 0.1 :duration 3/10}
    {:signal :break :duration 2/10}
    {:signal :B :intensity 0.1 :duration 3/10}
])

(def input-signals [:A :B :C])

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
;TODO: Can I refactor this?

(defn process-input [input] 
    (let [input-sequence (into [] (filter #(not (= (:signal %) :break)) input))
          first-two (make-first-two input-sequence)
          rest (filter #(not (contains? (set first-two) %)) input-sequence)]
        (println first-two)
        (into (into [] (if (> (count first-two) 1)
                        (into (translate-input-signal (first first-two) false) 
                                (translate-input-signal (second first-two) true))
                        (translate-input-signal (first first-two) true)))
            (reverse (reduce #(into %1 
                                    (translate-input-signal (second %2) 
                                        (mod (first %2) 3)))
                            []
                            (map-indexed vector (reverse rest)))))))