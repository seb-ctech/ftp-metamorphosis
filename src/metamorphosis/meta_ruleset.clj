(ns metamorphosis.meta-ruleset
    (:require 
        [metamorphosis.meta-ruleset.formal-system :as f]
        [metamorphosis.meta-ruleset.evolution :as evolve])
    (:gen-class)) 

(def build-random-axiom f/build-random-axiom)

(defn evolve-next-generation [theorem]
    (evolve/next-step theorem))
    
(defn first-generation [input-sequence]
    (f/build-axiom input-sequence))