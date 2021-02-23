(ns metamorphosis.core
  (:require [metamorphosis.util.core :as u]
            [metamorphosis.graphics.core :as gsys]
            [metamorphosis.event-listener.core :as esys]
            [metamorphosis.meta-ruleset.core :as msys])
  (:gen-class))

(def resolution [1000 1000])
(def evolving-interval 120)
(def max-generations 1)

(defn init []
  (let [state (gsys/setup-sketch)]
    (assoc  state :time {
                    :count 0 
                    :target evolving-interval}
            :last-gen 0
            :input-sequence [])))

(defn metamorph-loop
  [state]
    (let [state (-> state
                    u/update-util
                    gsys/update-graphics
                    esys/listen-for-event)
          event (esys/get-event state)
          restart? (:new-input? event)
          new-input (:input event)]
      (if restart?
        (do (println "Restarting! \n \n")
            (u/reset-time (assoc state :theorem (msys/first-generation new-input))))
        (if (and (contains? state :theorem) 
                 (u/time-up? state) 
                 (< (get-in state [:theorem :gen]) max-generations))
            (assoc state :theorem (msys/evolve-next-generation (:theorem state)))
            state))))

(defn -main [& args]
  (let [setup (if (nil? args) 
                init
                #(assoc (init) :mode (first args)))]
    (gsys/start-visualization resolution setup metamorph-loop)))