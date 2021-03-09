(ns metamorphosis.graphics.record-step
    (:require [quil.core :as q]
              [metamorphosis.util.core :as u]
              [metamorphosis.meta-ruleset.formal-system.core :as f]))

(defn save-generation 
    "A function that save a picture of every generation in the results folder"
    [state]
    (let [{theorem :theorem} state
          {gen :gen} theorem]
        (println "Saving!")
        (q/save (str u/results-root "/" (u/number-padding (u/get-result-counter)) "/" "gen-" gen ".jpg"))))