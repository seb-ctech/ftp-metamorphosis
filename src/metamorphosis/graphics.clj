(ns metamorphosis.graphics
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [metamorphosis.graphics.translation :as t]))

;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D:
;      1. Every generation represents one layer of complexity contained in higher levels
;      2. One Layer of complexity is made out of an algorithm that contains the algorithms of the lower levels

;returns a state
(defn setup-sketch []
    {})

(defn render-generation [generation]
    (t/make-quil (t/build-quil-algorithm generation)))

;renders the state that was updated
(defn draw-sketch [state]
    (if (contains? state :theorem)
        (render-generation (:theorem state))
        (q/background 0)))

; Programmatic creation of a quil sketch
(defn start-visualization [size update-loop]
    (q/sketch 
        :size size 
        :setup setup-sketch
        :update update-loop
        :draw draw-sketch
        :middleware [m/fun-mode]))