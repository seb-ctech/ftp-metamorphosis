(ns metamorphosis.core
  (:require [metamorphosis.util.core :as u]
            [metamorphosis.graphics.core :as gsys]
            [metamorphosis.event-listener.core :as esys]
            [metamorphosis.meta-ruleset.core :as msys])
  (:gen-class))

;=== PROGRAM SETTINGS =================

(def resolution [1000 1000])
(def evolving-interval 10)
(def max-generations 4)

;======================================

(defn init 
  "Function that sets the initial state for the program"
  []
  (let [state (gsys/setup-sketch)]
    (assoc state 
      :time {
        :count 0 
        :target evolving-interval}
      :input-sequence []
      :max-gen max-generations)))

(defn metamorph-loop
  "The main program loop which functions as wrapper for the three sub-systems: 
  Event-listener, Meta-Evolving Formal System and Graphical Translation"
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
            (u/update-result-log)
            (u/reset-time (assoc state :theorem (msys/first-generation new-input)
                                       :last-gen nil)))
        (if (and (contains? state :theorem) 
                 (u/time-up? state) 
                 (< (get-in state [:theorem :gen]) max-generations))
            (assoc state :theorem (msys/evolve-next-generation (:theorem state)))
            state))))

(defn -main 
  "Main function that starts a quil window and assigns to it the initialization function and main loop"
  [& args]
  (let [setup (if (nil? args) 
                init
                #(assoc (init) :mode (first args)))]
    (gsys/start-visualization resolution setup metamorph-loop)))