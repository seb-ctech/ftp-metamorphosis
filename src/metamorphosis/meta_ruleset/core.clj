(ns metamorphosis.meta-ruleset.core
    (:require 
        [metamorphosis.meta-ruleset.formal-system.core :as f]
        [metamorphosis.meta-ruleset.mutation :as m]
        [metamorphosis.meta-ruleset.translation :as tr])
    (:gen-class)) 

(defn evolve-next-generation
    "A function that progresses the structure from the previous generation to the next"
    [structure]
        {:gen (inc (:gen structure)) 
            :sequence (m/meta-mutate structure)})
    
(defn first-generation 
    "A function that produces the first generation by a given user input"
    [input-sequence]
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