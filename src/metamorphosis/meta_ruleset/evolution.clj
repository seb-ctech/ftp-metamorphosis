(ns metamorphosis.meta-ruleset.evolution
    (:require 
        [metamorphosis.meta-ruleset.formal-system :as f]
        [metamorphosis.meta-ruleset.mutation :as m]))

(defn next-step [structure]
    (println "Next generation")
        {:gen (inc (:gen structure)) 
         :sequence (m/meta-mutate structure)})

;==== TEST FUNCTIONS ======

(defn motif->phrase []
    (next-step (f/build-random-axiom)))

(defn motif->passage []
    (next-step (motif->phrase)))

(defn motif->movement []
    (next-step (motif->passage)))

(defn motif->final []
    (next-step (motif->movement)))