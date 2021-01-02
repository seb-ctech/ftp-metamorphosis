(ns metamorphosis.l-system
    (:gen-class))

;TODO: Define Alphabet
;TODO: Implement interfacing rules between L-System (String of Characters) and Graphic-Algorithms:
;      an abstract data structure which can be translated to a specific graphical implementation
;TODO: Implement function to create axiom from input-data-structure provided by the input-processor
;TODO: Implement meta-ruleset: that uses macros derived 
;      from the string of the current generation and when evaluated produces
;      the string for the next generation

(def axiom [:a :b :c :d])

(def alphabet ["A" "B" "C" "D" "E" "F" "G" "H" "I" "J" "K" "L" "M" "N" "O" "P" "Q" "R" "S" "T" "U" "V" "W" "X" "Y" "Z"])

(defn build-random-axiom 
    []
    (filter (fn [a] (< (rand) 0.2)) alphabet))

(defn parse-input 
    [input] input)