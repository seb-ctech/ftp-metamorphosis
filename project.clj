(defproject metamorphosis "0.1.0-SNAPSHOT"
  :description "An Art installation about the meaning of Meta in Art and Programming"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/core.async "1.3.610"]
                 [quil "3.1.0"]
                 [org.clojars.noodle-incident/leap-linux-native-deps "1.0.0"]
                 [rogerallen/leaplib "2.0.2"]]
  :resource-paths ["resources" "resources/glsl"]
  :main metamorphosis.core)
