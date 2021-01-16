(ns metamorphosis.meta-ruleset.formal-system)

;TODO: Define Alphabet as abstract structure that can be used to define mutating rules and graphic commands
;TODO: Implement function to create axiom from input-data-structure provided by the "event-listener" module

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

(defn process-input [input] 
    (reduce #(conj %1 (str %2)) [] input))