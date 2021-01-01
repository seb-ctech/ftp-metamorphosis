(ns metamorphosis.l-system)

;TODO: Define Alphabet
;TODO: Implement interfacing rules between L-System (String of Characters) and Graphic-Algorithms:
;      an abstract data structure which can be translated to a specific graphical implementation
;TODO: Implement function to create axiom from input-data-structure provided by the input-processor
;TODO: Implement meta-ruleset: that uses macros derived 
;      from the string of the current generation and when evaluated produces
;      the string for the next generation

(def axiom [:a :b :c :d])

(defn parse-input 
    [input] input)