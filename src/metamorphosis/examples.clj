(ns metamorphosis.examples
    (:require [quil.core :as q]
             [quil.middleware :as m]
             [metamorphosis.examples.line_dancers :as dancer]
             [metamorphosis.examples.tree :as tree]
             [metamorphosis.examples.colorfall :as colorf]))


            ; Example sketches

(def dance '(q/defsketch dancesketch
    :host "host"
     :size [500 500]
     :setup dancer/setup
     :draw dancer/draw))

(def rtree '(q/defsketch tree
    :host "host"
    :size [500 500]
    :setup tree/setup
    :update tree/update-state
    :draw tree/draw-state
    :key-pressed tree/key-pressed
    :renderer :p2d
    :middleware [m/fun-mode]))

(def cfall '(q/defsketch colorjoy
    :host "host"
    :size [500 500]
    :setup colorf/setup
    :draw colorf/draw))