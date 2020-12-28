; 4. Exercise is about building complex data-structures and applying algorithms that do useful conceptuals things with it,
; with the built-in clojure functions, map, reduce, etc...
(ns exercises.data-structures)

(def filename "src/exercises/csv/suspects.csv")
(def vamp-keys [:name :glitter-index])

(def nested-map {:x {:x1 {:x2 3}}})

(def get-nested-value (comp :x2 :x1 :x))

(def nested-value-plus-3 (comp #(+ 3 %) :x2 :x1 :x))

(defn sleepy-identity
    [x]
    (Thread/sleep 1000)
    x)

; Once it get's evaluated once, it wil save the output for every unique input for successive calls
(def memo-sleepy-identity (memoize sleepy-identity))

(defn str->int [str]
    (Integer. str))

(def conversions {:name identity :glitter-index str->int})

(defn conversion [vamp-key value]
    ((get conversions vamp-key) value))

(defn parse
    "Convert a CSV into rows of columns"
    [string]
    (map #(clojure.string/split % #",")
        (clojure.string/split string #"\n")))

(defn mapify
    "Return a sequence of Maps :name ... :index ..."
    [rows]
    (map (fn [unmapped-row]
            (reduce (fn [row-map [vamp-key value]]
                        (assoc row-map vamp-key (convert vamp-key value)))
                {}
                (map vector vamp-keys unmapped-row)))
        rows))

(defn glitter-filter
    [minimum-glitter records]
    (filter #(>= (:glitter-index %) minimum-glitter) records))