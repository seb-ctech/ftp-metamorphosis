(ns test.exercises
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [examples.sketch :as simple]
              [examples.recursion :as rec]
              [examples.glsl-parsing :as glsl]))

(defn test-ns [] (ns-aliases *ns*))

; Example sketches
; Can't use alias, because when it gets returned the context where it gets evaluated misses the namespace alias.

(defn simple-sketch [] (q/defsketch simplesketch
    :host "host"
     :size [500 500]
     :setup s/setup
     :draw s/draw))

(defn recursive-piece [] (q/defsketch recursion
    :host "host"
    :size [500 500]
    :setup rec/setup
    :update rec/update
    :draw rec/draw
    :renderer :p2d))

(defn glsl-piece [] (q/defsketch glslcomposer
    :host "host"
    :size [500 500]
    :setup glsl/setup
    :update glsl/update
    :draw glsl/draw))

(defn run-example [] (cfall))