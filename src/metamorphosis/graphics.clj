(ns metamorphosis.graphics
    (:require [quil.core :as q]
              [quil.middleware :as m]
              [metamorphosis.l-system :as ls]
              [test.examples :as ex]))

;TODO: Find visual translation made out of composable functions that is fitting for a beginning motif. Ideally 3D

;returns a state
(defn setup [])

;updates the state per frame
(defn update [state])

;renders the state that was updated
(defn draw [state])

; Main Entry point for Quil Sketches
(defn metamorph [] (q/defsketch metamorphosis
    :title "Metamorphosis"
    :size [1000 1000]
    :setup setup
    :update update
    :draw draw))