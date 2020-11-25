(ns metamorphosis.graphics
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [metamorphosis.l-system :as ls]
              [test.examples :as ex]))

;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D

(defn setup []
; Set frame rate to 30 frames per second.
    (q/frame-rate 30)
    ; Set color mode to HSB (HSV) instead of default RGB.
    (q/color-mode :hsb)
    ; setup function returns initial state. It contains
    ; circle color and position.
    {:color 0
     :angle 0})

(defn f [t]
    (let [r (* 200 (q/sin (+ t 1)) (q/cos t))]
    [(* r (q/sin (* t 0.2)))
        (* r (q/cos (* t 0.2)))]))

(defn draw-state [state]
    (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
        (let [t (/ (q/frame-count) 10)]
        (q/line (f t) 
                (f (+ t 0.1))))))

(defn update-state [state]
; Update sketch state by changing circle color and position.
)

(defn draw-plot [f from to step]
    (doseq [two-points (->> (range from to step)
                        (map f)
                        (partition 2 1))]
    (apply q/line two-points)))

; Main Entry point for Quil Sketches
; 1. Main Metamorphosis visuals
(defn metamorph [] (q/defsketch metamorphosis
    :title "Metamorphosis"
    :size [500 500]
    ; setup function called only once, during sketch initialization.
    :setup setup
    ; update-state is called on each iteration before draw-state.
    :update update-state
    :draw draw-state
    :features [:keep-on-top]
    ; This sketch uses functional-mode middleware.
    ; Check quil wiki for more info about middlewares and particularly
    ; fun-mode.
    :middleware [m/fun-mode]))