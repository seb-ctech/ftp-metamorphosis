; 2. Exercise implementing and inventing a recursive algorithm and piece.
(ns exercises.recursion
    (:require [quil.core :as q :include-macros true]))

(def min-square-size 10)
(def nested-ratio 0.2)

(defn initial-quad [] [{:x 0 :y 0}, {:x (q/width) :y 0}, {:x (q/width) :y (q/height)}, {:x 0 :y (q/height)}])

(defn get-quad-size [quad]
    (let [p1 (first quad)
          p2 (first (rest quad))]
        (q/dist (:x p1) (:y p1) (:x p2) (:y p2))))

(defn get-inset-corner [outer-corner center ratio]
    (let [r (Math/random)
          offset-x (* (- (:x outer-corner) (:x center)) ratio r)
          offset-y (* (- (:y outer-corner) (:y center)) ratio r)]
        {:x (+ (:x center) offset-x)
         :y (+ (:y center) offset-y)}))

(defn draw-nested-quad [outer-quad center step]
    (let [inner-quad (map #(get-inset-corner % center step) outer-quad)
          in-out-pairs (map #(assoc (assoc {} :inner-point %1) :outer-point %2) inner-quad outer-quad)]
        (doseq [pair in-out-pairs]
            (let [inner-point (:inner-point pair)
                  outer-point (:outer-point pair)]
                (q/line (:x inner-point) (:y inner-point) (:x outer-point) (:y outer-point))))
        (let [points (reduce #(into %1 [(:x %2) (:y %2)]) [] inner-quad)]
            (apply q/quad points))
        inner-quad))

(defn draw-quad-ception [x y increment]
    (println increment)
    (loop [outer-quad (initial-quad)
          opacity 0]
        (q/stroke 255 (min opacity 255))
        (when (>= (get-quad-size outer-quad) 2) 
            (recur (draw-nested-quad outer-quad {:x x :y y} (- 1 nested-ratio)) (+ opacity increment)))))

(defn setup []
    {:reach 20})

(defn update[state]
    (if (q/mouse-pressed?)
        (assoc state :reach (min (+ (:reach state) 0.5) 200))
        (assoc state :reach (max (- (:reach state) 0.5) 20))))

(defn draw [state]
    (let [x (q/mouse-x)
          y (q/mouse-y)]
        (q/background 20 0 0)
        (q/stroke 255)
        (q/no-fill)
        (draw-quad-ception x y (:reach state))))