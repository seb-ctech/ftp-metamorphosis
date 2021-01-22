(ns metamorphosis.core
  (:require [metamorphosis.util :as u]
            [metamorphosis.graphics :as gsys]
            [metamorphosis.event-listener :as esys]
            [metamorphosis.meta-ruleset :as msys])
  (:gen-class))

(def resolution [500 500])
(def evolving-interval 20)

(defn init []
  (let [state (gsys/setup-sketch)]
    (assoc state :time {
                    :count 0 
                    :target evolving-interval})))

(defn metamorph-loop
  [state]
    (let [event (esys/listen-for-event)
          restart? (:new-input? event)
          new-input (:input event)
          state (gsys/update-graphics (u/update-util state))]
      (if restart?
        (assoc state :theorem (msys/first-generation new-input))
        (if (and (contains? state :theorem) (u/time-up? state))
          (do
            (println "new theorem!")
            (println (:theorem state))
            (assoc state :theorem (msys/evolve-next-generation (:theorem state))))
           state))))
    
(defn -main [& args]
  (let [setup (if (nil? args) 
                init
                #(assoc (init) :mode (first args)))]
    (gsys/start-visualization resolution setup metamorph-loop)))

;=== Entry point for Developing without event-listener ====

(defn start 
  ([] (gsys/start-visualization resolution #(assoc (init) :theorem (msys/build-random-axiom)) metamorph-loop))
  ([input] 
      (gsys/start-visualization resolution #(assoc (init) :theorem (msys/first-generation 
                                                                      (if (and (keyword? input) (= input :standard))  
                                                                          esys/test-input
                                                                          (esys/command-line input)))) metamorph-loop)))