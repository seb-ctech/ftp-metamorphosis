(ns metamorphosis.meta-ruleset.formal-system.print)


(def long-separator (apply str (repeat 60 "-")))

(def short-separator (apply str (repeat 5 "-")))

(def next-entry " -> ")

(defn top-level-generation
    "A function that prints the header in form of the top-level generation"
    ([generation]
        (str "\n" "Generation" " " generation "\n" long-separator "\n"))
    ([generation repetitions]
        (str "\n" "Generation" " " generation 
            " " 
            "(" "O" "-" (apply str 
                                (repeat (dec repetitions) "X"))
            ")" "\n" long-separator "\n")))


(defn nested-prefix 
    "A function that returns the necessary indentation by nesting level"
    [top current]
    (apply str (repeat (- top current) "\t")))

(defn gen-variation 
    "A function that prints a sub header for the current copy within a generation"
    [top current variation]
    (str 
        (if (= (:type variation) :original)
            "O | Original"
            (str "X | Mutation-" (:index variation)))
        " " "(" "GEN-" current
        (if (nil? (:rep variation))
            "" 
            (if (= (:type variation) :original)
                (str ", " "o" "-" 
                    (apply str 
                        (repeat (dec (:rep variation)) "x")))
                ""))
        ")" "\n" ))

(defn command->string 
    "A function that converts a command class to a printable name"
    [amount command]
    (str (case (:class command)
                :transform "TRANS"
                :property "PROP"
                :unit "UNIT"
                :amount "AMOUNT"
                "")
          "-"
          (:index command)
          " "
          "("
          (:index amount)
          ")"))


(defn sequence->string 
    "A function that prints a sequence in a readable format"
    [sequence]
    (let [groups (partition 2 (filter #(not (= (:class %) :glue)) sequence))
          last (dec (count groups))]
        (apply str (map-indexed (fn [i entry] 
                                    (let [amount (first entry)
                                        command (second entry)]
                                        (str (command->string amount command) 
                                            (if (< i last) 
                                                next-entry 
                                                "")))) 
                      groups))))

;FIXME: Is a separate function to avoid a dependency cycle with core
(defn get-repetitions 
    "A function that reads the glue of a sequence to get the amount of copies."
    [sequence]
    (first (:values (first sequence))))

(defn recursive-sequence 
    "A function that recursively prints nested sequence structures"
    [top current sequence]
    (loop [prefix []
           remaining sequence
           index 0
           final-string ""]
        (let [next (first remaining)]
            (if (> (count remaining) 0)
                (if (contains? next :sequence)
                    (let [next-index (:gen next)
                          prefix (filter #(not (= (:class %) :glue)) prefix)]
                        (recur 
                            [] 
                            (rest remaining)  
                            (inc index) 
                            (str final-string
                                (nested-prefix top current)
                                (if (= index 0)
                                    (gen-variation top next-index {:type :original :rep (get-repetitions (:sequence next))})
                                    (gen-variation top next-index {:type :variation :index index}))
                                (nested-prefix top current)
                                (if (or (empty? prefix) (empty? (first prefix)))
                                    "" 
                                    (str (sequence->string prefix) next-entry))
                                (if (= (:gen next) 0)
                                    (str "[" (sequence->string (:sequence next)) "]" "\n")
                                    (str "\n" (recursive-sequence top (dec current) (:sequence next))))
                                (nested-prefix top current) short-separator "\n" "\n")))
                    (recur (conj prefix next)
                           (rest remaining)
                           index
                           final-string))
            final-string))))