(ns metamorphosis.meta-ruleset.evolution
    (:require 
        [metamorphosis.meta-ruleset.formal-system :as f]
        [metamorphosis.meta-ruleset.mutation :as m]))

(defn next-step [structure]
    (println (str "Previous Generation:" " " structure))
    (reduce into 
        (m/level-glue) 
        [[structure] (m/compose-mutations structure)]))

;==== TEST FUNCTIONS ======

(defn motif->phrase []
    (next-step (f/build-random-axiom)))

(defn phrase->passage []
    (next-step (motif->phrase)))

(defn passage->movement []
    (next-step (phrase->passage)))

(defn motif->final []
    (next-step (passage->movement)))