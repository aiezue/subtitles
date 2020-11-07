(ns aiezue.subtitle-corpus.create
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:gen-class))

(defn rm
  "delete existing corpus, and create a new one afterwards"
  [& _]
  (io/delete-file "subtitles_corpus.dat" true))

(defn mk
  "clean subtitles and make it a tiny text corpus"
  [& _]
  (let [fs (next (file-seq (io/file "srt")))
        ;; using regexp to clean the subtitles
        ;; time-stamp and scene numbers
        ts #"\d+\n\d{2}:\d{2}:\d{2},\d{3} --> \d{2}:\d{2}:\d{2},\d{3}"
        ;; html tags, for italics and bolds, etc
        ht #"</?.+?>"
        ;; empty lines
        el #"\n+"
        ;; functions to replace them
        ts! #(string/replace % ts "")
        ht! #(string/replace % ht "")
        el! #(string/replace % el "\n")]
    (for [f fs
          ;; slurp it and then clean it one by one
          :let [c (->> (slurp (str f))
                       (ts!)
                       (ht!)
                       (el!))]]
      (spit "subtitles_corpus.dat" c
            ;; append to ensure it will be a single file with all the contents
            :append true))))


(defn -main
  "put them up together"
  [& _]
  (rm)
  (mk))
