; 1. Exercise. Trying to implement an algorithm for a generative piece in clojure
(ns exercises.sketch
    (:require [quil.core :as q :include-macros true]))

(def number-of-cells 10)

(defn generate-grid []
    (let [cell-size (/ (q/width) number-of-cells)]
        (loop [cells [] x 0 y 0]
            (let [next-x (+ x cell-size)
                  next-y (+ y cell-size)
                  top-left {:x x :y y}
                  top-right {:x next-x :y y}
                  bottom-left {:x x :y next-y}
                  bottom-right {:x next-x :y next-y}
                  cell {:top-left top-left :top-right top-right :bottom-left bottom-left :bottom-right bottom-right}
                  new-cells (conj cells cell)]
                (if (< next-x (q/width))
                    (recur new-cells next-x y)
                    (if (< next-y (q/height))
                        (recur new-cells 0 next-y)
                        new-cells))))))

(defn draw-diagonal [corners flipped?]
    (if flipped?
        (q/line (:bottom-left corners) (:top-right corners))
        (q/line (:top-left corners) (:bottom-right corners))))

(defn setup []
    {:cells (generate-grid)})

(defn draw-state [state]
    (doseq [cell (:cells state)]
        (draw-diagonal cell (< (Math/random) 0.5))))