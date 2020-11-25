(ns metamorphosis.core
  (:require [metamorphosis.graphics :as g]))

;TODO: Implement overall-architecture

(defn -main [& args]
  (g/metamorph)
  (println args))