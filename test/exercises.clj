(ns test.exercises
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [exercises.sketch :as simple]
              [exercises.recursion :as rec]
              [exercises.glsl-parsing :as glsl]))

(defn test-ns [] (ns-aliases *ns*))

; Example sketches
; Can't use alias, because when it gets returned the context where it gets evaluated misses the namespace alias.

(defn simple-sketch [] (q/defsketch simplesketch
    :host "host"
     :size [500 500]
     :setup simple/setup
     :draw simple/draw-state
     :middleware [m/fun-mode]
     :mouse-clicked simple/mouse-clicked
     :key-pressed simple/key-pressed))

(defn recursive-piece [] (q/defsketch recursion
    :host "host"
    :size [500 500]
    :setup rec/setup
    :update rec/update
    :draw rec/draw
    :middleware [m/fun-mode]))

(defn glsl-piece [] (q/defsketch glslcomposer
    :host "host"
    :size [500 500]
    :setup glsl/setup
    :update glsl/update
    :draw glsl/draw))

;FIXME: Java > 9.0 produces an illegal reflective access when using ":renderer p2d".
; Can't use Shaders or 3D-Renderer for now!

(defn run-exercise [e] (e))

(run-exercise recursive-piece)