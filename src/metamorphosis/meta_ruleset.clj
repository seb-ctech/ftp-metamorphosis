(ns metamorphosis.meta-ruleset
    (:require 
        [metamorphosis.meta-ruleset.formal-system :as f]
        [metamorphosis.meta-ruleset.evolution :as evolve])
    (:gen-class)) 

(defn evolve-next-generation [theorem]
    (evolve/next-step theorem))
    
;TODO: Implement System that transforms input to initial theorem of meta-algorithm system
(defn first-generation [input-sequence]
    (f/process-input input-sequence))