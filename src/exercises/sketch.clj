; 1. Exercise. Trying to implement an algorithm for a generative piece in clojure
(ns exercises.sketch
    (:require [quil.core :as q :include-macros true]))

(def number-of-cells 5)

; Using lazy sequences to generate an infinite range of random numbers, that always stay the same
(defn generate-random-sequence []
    (map #(+ (Math/random) (- % %) (range))))

(defn generate-grid [rand]
    (let [cell-size (/ (q/width) number-of-cells)]
        (loop [cells [] x 0 y 0 i 0]
            (let [next-x (+ x cell-size)
                  next-y (+ y cell-size)
                  top-left {:x x :y y}
                  top-right {:x next-x :y y}
                  bottom-left {:x x :y next-y}
                  bottom-right {:x next-x :y next-y}
                  flipped (< (get rand i) 0.5)
                  cell {:top-left top-left :top-right top-right :bottom-left bottom-left :bottom-right bottom-right :flipped? flipped}
                  new-cells (conj cells cell)]
                (if (< next-x (q/width))
                    (recur new-cells next-x y (inc i))
                    (if (< next-y (q/height))
                        (recur new-cells 0 next-y (inc i))
                        new-cells))))))

(defn draw-diagonal [cell]
    (if (:flipped? cell)
        (q/line (:x (:bottom-left cell)) (:y (:bottom-left cell)) (:x (:top-right cell)) (:y (:top-right cell)))
        (q/line (:x (:top-left cell)) (:y (:top-left cell)) (:x (:bottom-right cell)) (:y (:bottom-right cell)))))

;Layer of Abstraction to better control the outcome later
(defn draw-cell [cell]
    (draw-diagonal cell))

(defn setup []
    (let [rand (generate-random-sequence)]
    {   :random-seq rand
        :cells (generate-grid rand)}))

(defn draw-state [state]
    (q/background 255)
    (doseq [cell (:cells state)]
        (draw-cell cell)))

(defn mouse-clicked [state]
    (assoc state :cells (generate-grid (:random-seq state))))