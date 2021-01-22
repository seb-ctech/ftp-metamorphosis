(ns metamorphosis.event-listener.input.leap-motion
    (:require [clojure-leap.core :as leap]))

(def controller (leap/controller))

;TODO: Implement Leap-Motion Input