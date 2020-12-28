(ns metamorphosis.graphics
    (:require [quil.core :as q]
              [quil.middleware :as m]))

;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D:
;      1. Every generation represents one layer of complexity contained in higher levels
;      2. One Layer of complexity is made out of an algorithm that contains the algorithms of the lower levels

;returns a state
(defn setup-sketch [])

;updates the state per frame
(defn update-sketch [])

;renders the state that was updated
(defn draw-sketch [])

; Main Entry point for Quil Sketches
(defn metamorph [] (q/defsketch metamorphosis
    :title "Metamorphosis"
    :size [1000 1000]
    :setup setup-sketch
    :update update-sketch
    :draw draw-sketch))