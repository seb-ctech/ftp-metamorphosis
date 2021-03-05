(ns metamorphosis.meta-ruleset.formal-system.core
    (:require 
        [metamorphosis.meta-ruleset.formal-system.print :as p]))

;=== PROTOTYPE OF AN ABSTRACT DATA STRUCTURE ===

; One generation contains the generation count for the scope a sequence of objects that define a class,
; that has a different meaning by the translation context it is used in and an index for the list of 
; commands of the same class. Classes can be of type: body, transform, property, amount.

; UNIT: Represents a unit of the final composition that is perceptible: [GraphX] Draws some form of geometry; [Mutation] builds some sequence
; TRANSFORM: Changes the relation between units in some form
; PROPERTY: Some property that the next units will have
; AMOUNT: Some amount that either the next Unit, Transform or Property will have. Default is "0"

(def alphabet [:unit :transform :property :amount])

(defn random-entry []
    {:class (get alphabet 
                 (rand-int (count alphabet)))
     :index (rand-int 5)})

(defn build-axiom [sequence]
    "Function that builds an axiom out of a sequence by appending gen 0"
    {:gen 0 :sequence sequence})

(defn glue 
    "A function that returns an addition helper class that behaves as a fix and is aware of all copies"
    ([n]
        {:class :glue :values (list n)})
    ([n i]
        {:class :glue :values (list n i)}))

(defn build-command-pair [class index amount]
    (list {:class :amount :index amount}
          {:class class :index index}))

(defn read-command-pair [pair]
    {:class (:class (second pair))
        :index (:index (second pair))
        :amount (:index (first pair))})

(defn build-random-axiom 
    ([]
        (build-random-axiom (+ (rand-int 6) 3)))
    ([length]
        (loop [sequence []]
            (if (< (count sequence) length)
                (recur (conj sequence 
                            (random-entry)))
                 (build-axiom sequence)))))

(defn command-class? [class entry]
    (= (:class entry) class))

(defn has-lower-level? [entry]
    "A function that returns true of false, wether an entry has a lower-level sequence"
    (contains? entry :sequence))

(defn has-sub-seq? [sequence]
    (some has-lower-level? sequence))

(defn count-copies [sequence]
    (count (filter has-lower-level? sequence)))

(defn split-scopes 
    "A function that splits an input sequence into a prefix, the next lower-entry and the rest that comes after it"
    [sequence]
    (loop [prefix []
            remaining sequence]
        (if (> (count remaining) 0) 
            (if (has-lower-level? (first remaining))
                {:pre prefix :scope (first remaining) :rest (rest remaining)}
                (recur (conj prefix (first remaining)) (rest remaining)))
            {:pre prefix})))

(defn transform-> 
    "a function that modifies a ready sequence so that it can be easily transformed"
    [r-sequence]
    (if (has-sub-seq? r-sequence)
        (into (vector) (filter #(not (command-class? :glue %)) r-sequence))
        r-sequence))

(defn ready-> 
    "a function that takes a sequence and makes it ready for translation by applying glues"
    [sequence]
    (if (has-sub-seq? sequence)
        (let [copies (count-copies sequence)]
            (loop [split (split-scopes sequence)
                   remaining copies
                   r-seq []]
                (if (> remaining 0)
                    (let [index (inc (- copies remaining))
                        r-seq (conj  
                                    (into 
                                        (conj r-seq 
                                            (if (> index 1)
                                                (glue copies index)
                                                (glue copies)))
                                        (:pre split))
                                    (:scope split))]
                        (recur (split-scopes (:rest split))
                            (dec remaining)
                            r-seq))
                    r-seq)))
        sequence))
                    

(defn group-sequence 
    "A function that groups amount-command pairs, glues and sequence, so that it is possible to work with them"
    [sequence]
    (let [transformed (transform-> sequence)]
        (loop [split (split-scopes transformed)
               grouped []]
            (let [pre-grouped (if (:pre split)
                                  (into grouped (partition 2 (:pre split)))
                                  grouped)
                  append-seq (if (:scope split)
                                 (conj pre-grouped (:scope split))
                                 pre-grouped)]
                (if (:rest split)
                    (recur (split-scopes (:rest split)) 
                            append-seq)
                    append-seq)))))

(defn ungroup-sequence 
    "A function that flattens a grouped sequence"
    [grouped]
    (reduce (fn [final entry]
                (if (seq? entry)
                    (into final entry)
                    (conj final entry))) 
        [] 
        grouped))

(defn create-sequence 
    "A function that given a list of parameters builds a valid sequence"
    [])

(defn print-theorem [theorem]
    (let [top-level (if (= (:gen theorem) 0)
                        (p/top-level-generation (:gen theorem))
                        (p/top-level-generation (:gen theorem) (p/get-repetitions (:sequence theorem))))
         output (str "\n" top-level "\n"
                    (if (= (:gen theorem) 0)
                        (p/sequence->string (:sequence theorem))
                        (p/recursive-sequence (:gen theorem) 
                                              (:gen theorem) 
                                              (:sequence theorem)))
                        "\n")]
        (do (spit "resources/theorem.log" output))))