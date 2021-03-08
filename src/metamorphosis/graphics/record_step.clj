(ns metamorphosis.graphics.record-step
    (:require [quil.core :as q]
              [metamorphosis.util.core :as u]
              [metamorphosis.meta-ruleset.formal-system.core :as f]))

(def root "resources/results")
(def padding 4)


(defn number-padding [n]
    (let [toadd (- padding (count (str n)))]
        (str (apply str (repeat toadd 0)) (str n))))

(defn save-generation [state]
    (let [{theorem :theorem} state
          {gen :gen} theorem]
        (println "Saving!")
        (q/save (str root "/" (number-padding (u/get-result-counter)) "/" "gen-" gen ".jpg"))
        (when (= gen (:max-gen state))
            (f/print-theorem theorem (str root "/" (number-padding (u/get-result-counter)))))))