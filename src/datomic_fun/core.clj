(ns datomic-fun.core
  (:use clojure.pprint
        [datomic.api :only [q db] :as  d]))

(def uri "datomic:mem://mages")

;; create database
;; explain, what is a database?
(d/create-database uri)

(def conn (d/connect uri))

(def schema
  ;; what's db/id?

  ;; Mage attributes
  [{:db/id #db/id[:db.part/db]
    :db/ident :mage/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/fulltext true
    :db/doc "A mage's name"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :mage/school
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "School of magic mage belongs to"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :mage/spells
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db/doc "Spells in mage's repertoire"
    :db.install/_attribute :db.part/db}

   ;; schools of magic
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/earth]
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/fire]
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/wind]
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/water]
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/life]
   [:db/add #db/id[:db.part/user] :db/ident :magic.school/mind]
   
   ;; Spell attributes
   {:db/id #db/id[:db.part/db]
    :db/ident :spell/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity
    :db/doc "A unique spell name (upsertable)"
    :db.install/_attribute :db.part/db}

   {:db/id #db/id[:db.part/db]
    :db/ident :spell/school
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one
    :db/doc "School of magic spell belongs to"
    :db.install/_attribute :db.part/db}

    {:db/id #db/id[:db.part/db]
     :db/ident :spell/casting-method
     :db/valueType :db.type/ref
     :db/cardinality :db.cardinality/one
     :db/doc "A spell casting method enum value"
     :db.install/_attribute :db.part/db}
    
    [:db/add #db/id [:db.part/user] :db/ident :spell.casting-method/verbal]
    [:db/add #db/id [:db.part/user] :db/ident :spell.casting-method/manual]
    [:db/add #db/id [:db.part/user] :db/ident :spell.casting-method/thought]])

@(d/transact conn schema)

(def seed-data
  [
   ;; Add a fire spell
   {:db/id #db/id[:db.part/user -1000001]
    :spell/name "Fire tickle"
    :spell/school :magic.school/fire
    :spell/casting-method :spell.casting-method/manual}
   
   ;; Add a mage who has the fire tickle spell
   {:db/id #db/id[:db.part/user -1000002]
    :mage/name "Jonathan Strange"
    :mage/school :magic.school/fire
    :mage/spells [#db/id[:db.part/user -1000001]]}])

@(d/transact conn seed-data)