(ns metamorphosis.meta-ruleset.core
    (:require 
        [metamorphosis.meta-ruleset.formal-system :as f]
        [metamorphosis.meta-ruleset.evolution :as evolve]
        [metamorphosis.meta-ruleset.translation :as tr])
    (:gen-class)) 

;TODO: Remove, only needed for development
(def build-random-axiom f/build-random-axiom)

(defn evolve-next-generation [theorem]
    (evolve/next-step theorem))
    
(defn first-generation [input-sequence]
    (f/build-axiom (tr/process-input input-sequence)))