(ns aiezue.subtitle-corpus.pos
  (:require [opennlp.nlp :refer [make-pos-tagger make-tokenizer]]
            [com.hypirion.clj-xchart :as c]
            [clojure.pprint :refer [pprint]])
  (:gen-class))

(defn pos
  "use opennlp to show pos taggings"
  [& _]
  (let [pos-tag (make-pos-tagger "models/en-pos-maxent.bin")
        tokenize (make-tokenizer "models/en-token.bin")
        s (slurp "subtitles_corpus.dat")
        pos (pos-tag (tokenize s))
        fq (frequencies (vals (into (sorted-map) pos)))]
    (pprint pos)
    (pprint fq)
    (c/view (c/pie-chart fq {:title "POS Pie Chart" :render-style :donut :theme :ggplot2}))))

(defn -main
  "paint it on the walls"
  [& _]
  (pos))
