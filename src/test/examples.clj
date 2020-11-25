(ns test.examples
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [examples.line_dancers :as dancer]
              [examples.tree :as tree]
              [examples.colorfall :as colorf]))

(defn test-ns [] (ns-aliases *ns*))

; Example sketches
; Can't use alias, because when it gets returned the context where it gets evaluated misses the namespace alias.

(defn dance [] (q/defsketch dancesketch
    :host "host"
     :size [500 500]
     :setup dancer/setup
     :draw dancer/draw))

(defn rtree [] (q/defsketch rectree
    :host "host"
    :size [500 500]
    :setup tree/setup
    :update tree/update-state
    :draw tree/draw-state
    :key-pressed tree/key-pressed
    :renderer :p2d
    :middleware [m/fun-mode]))

(defn cfall [] (q/defsketch colorjoy
    :host "host"
    :size [500 500]
    :setup colorf/setup
    :draw colorf/draw))

(defn run-example [] (cfall))