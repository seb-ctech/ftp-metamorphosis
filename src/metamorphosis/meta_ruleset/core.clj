(ns metamorphosis.meta-ruleset.core
    (:require 
        [metamorphosis.meta-ruleset.formal-system.core :as f]
        [metamorphosis.meta-ruleset.mutation :as m]
        [metamorphosis.meta-ruleset.translation :as tr])
    (:gen-class)) 

(defn next-step [structure]
    (println "Next generation")
        {:gen (inc (:gen structure)) 
            :sequence (m/meta-mutate structure)})

(defn evolve-next-generation [theorem]
    (next-step theorem))
    
(defn first-generation [input-sequence]
    (println "Sequence: " input-sequence)
    (println "Motif: " )
    (doseq [entry (tr/process-input input-sequence)] (println entry))
    (f/build-axiom (tr/process-input input-sequence)))

;==== TEST FUNCTIONS ======

(defn motif->phrase []
    (next-step (f/build-random-axiom)))

(defn motif->passage []
    (next-step (motif->phrase)))

(defn motif->movement []
    (next-step (motif->passage)))

(defn motif->final []
    (next-step (motif->movement)))