package com.bstek.designer.core.config

import java.util.HashMap
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EClass
import org.eclipse.emf.ecore.EDataType
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EPackage
import org.eclipse.emf.ecore.EcoreFactory
import org.eclipse.emf.ecore.EcorePackage
import org.eclipse.emf.ecore.EStructuralFeature
import com.bstek.dorado.idesupport.model.Property
import com.bstek.dorado.idesupport.model.Rule
import com.bstek.dorado.idesupport.model.Child
import com.bstek.dorado.idesupport.model.RuleSet
import scala.beans.BeanProperty
import scala.collection.JavaConversions._
import com.intellij.openapi.project.Project
import com.bstek.designer.core.config.util.DoradoRuleSetConfig
import java.lang.String
import scala.Predef.String
import scala.Boolean
import scala.Predef.Set

/**
 * @author robin
 */
class DoradoRulesConfigImpl(@BeanProperty val project: Project) extends DoradoRulesconfig {


  var simpleRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule] = new HashMap()
  var complexRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule] = new HashMap()

  @BeanProperty var simpleEClasses: java.util.Map[DoradoRuleMultiKey, EClass] = new HashMap()
  @BeanProperty var complexEClasses: java.util.Map[DoradoRuleMultiKey, EClass] = new HashMap()
  @BeanProperty var superEClasses: java.util.Map[Any, EClass] = new HashMap()
  @BeanProperty var auxiliaryEClasses: java.util.Map[java.lang.String, EClass] = new HashMap()
  @BeanProperty var entityEClasses: java.util.Map[java.lang.String, EClass] = new HashMap()
  val noAuxiliaryRootElements = Set("Model",
    "ViewConfig", "Arguments", "DbModel", "Table", "AutoTable", "SqlTable",
    "StoredProgram", "Parameters", "ProgramParameter", "TableColumn",
    "TableKeyColumn", "SqlTableColumn", "AutoTableColumn",
    "JoinTables", "FromTables", "JoinTable", "FromTable", "@Where",
    "Orders", "Order", "SimpleMatchRule", "JunctionMatchRule", "SqlMatchRule", "BetweenMatchRule")

  var modelRegister: java.util.Map[EClass, DoradoConfigRulesModelMeta] = new HashMap()

  var clientEventEClass: EClass = _
  var propertyEClass: EClass = _
  var valueEClass: EClass = _
  var ruleSet: RuleSet = _
  var auxiliaryRule: Rule = _

  @BeanProperty var ecorePackage: EcorePackage = EcorePackage.eINSTANCE
  @BeanProperty var ecoreFactory: EcoreFactory = EcoreFactory.eINSTANCE
  var ePackage: EPackage = createDynamicEPackage()
  var eDataType: EDataType = ecorePackage.getEString()

  def createDynamicEPackage(): EPackage = {
    ePackage = ecoreFactory.createEPackage()
    ePackage.setName(PACKAGE_NAME)
    ePackage.setNsURI(NS_URI)
    return ePackage
  }

  def initialize() {
    ruleSet = getRuleSet(project)
    val rulesMap: java.util.Map[String, Rule] = ruleSet.getRuleMap
    rulesMap.foreach(kv => if (kv._2.getChildren().size() == 0) simpleRules.put(kv._2.getName(), kv._2) else complexRules.put(kv._2.getName(), kv._2))
    // --全局EClass定义
    // --Property Class定义
    createPropertyEClass()
    // --ClientEvent Class定义
    createClientEventEClass()
    // --Auxiliary Class定义
    createAuxiliaryEClasses()
    // --Value Class定义
    createValueEClasses()
    // --Entity Class定义
    createEntityEClasses()
    // --非引用独立EClass定义
    createSimpleEClasses(simpleRules)
    // --存在引用EClass定义
    createComplexEClasses(complexRules)
    complete(ecoreFactory)
  }

  def createComplexEClasses(complexRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule]) {
    complexRules.foreach(kv => if (!complexEClasses.containsKey(new DoradoRuleMultiKey(kv._2.getName, kv._2))) createComplexEClass(complexRules, kv._2, kv._2.getName))
  }

  def createComplexEClass(complexRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule], complexRule: Rule, className: String): org.eclipse.emf.ecore.EClass = {
    val complexEClass = ecoreFactory.createEClass()
    try {
      val className = complexRule.getName
      complexEClass.setName(className)
      complexEClasses.put(new DoradoRuleMultiKey(className, complexRule), complexEClass)
      //--注册模型附加信息
      registerModel(complexEClass, complexRule)
      //--定义attribute属性
      addAttributes(complexEClass, complexRule)
      //--ClientEvent引用定义
      val clientEventMap = complexRule.getClientEvents
      if (!clientEventMap.isEmpty) {
        addClientEventReference(complexEClass, complexRule)
      }
      // --定义property reference
      addPropertyReference(complexEClass)
      // --定义子节点reference
      addReferences(complexRules, complexRule, complexEClass)
      // --占位符引用
      addAuxiliaryReference(complexRule, className, complexEClass)
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
    return complexEClass
  }

  def addReferences(complexRules: java.util.Map[java.lang.String, Rule], rule: Rule, complexEClass: EClass) {
    val name = rule.getName()
    val ownChildren = rule.getChildren()
    val childrenMap = new java.util.LinkedHashMap[String, Child]()
    val iter = ownChildren.values.iterator
    while (iter.hasNext()) {
      val child = iter.next()
      // --是否0-*聚合
      val isAggregated = child.isAggregated()
      val isFixed = child.isFixed()
      val reserve = child.getReserve()
      getSubChildren(child, childrenMap, isAggregated, isFixed, reserve)
    }

    val childrenIter = childrenMap.values.iterator
    while (childrenIter.hasNext) {
      val child = childrenIter.next
      val parentChildObject = child.getUserData()
      val childRule = child.getRule()
      val ownName = childRule.getName()

      val ownerEClass = getOwnerEClass(name, ownName, childRule, complexEClass)
      val aggregated = java.lang.Boolean.valueOf(child.isAggregated())
      // --如果是聚合节点,就可能存在同一类型多个不同引用,必须指定同一父类型,以便使其对应树节点自由移动
      if (aggregated.booleanValue()) {
        createSuperEClass(rule, ownerEClass, complexEClass, aggregated)
      } else {
        createReference(complexEClass, ownerEClass, ownName, aggregated)
      }
      // --将Reference(引用)附加信息附着在其所属元模型的附加信息上,附加信息为子节点Child对象及父Child对象,及子节点名称
      addMetaOfReference(complexEClass, new DoradoReferenceMeta(
        parentChildObject, child, ownName))
    }
  }

  def getSubChildren(child: Child, childrenMap: java.util.LinkedHashMap[String, Child],
                     isAggregated: Boolean, isFixed: Boolean, reserve: String) {
    try {
      val subChildrenRules = child.getConcreteRules()
      val iter = subChildrenRules.iterator
      while (iter.hasNext) {
        val subChildRule = iter.next()
        val childName = subChildRule.getName()
        val subChild = new Child(childName)
        subChild.setAggregated(isAggregated)
        subChild.setFixed(isFixed)
        subChild.setReserve(reserve)
        subChild.setRule(subChildRule)
        subChild.setUserData(child)
        childrenMap.put(childName, subChild)
      }
    } catch {
      case ex: Exception => ex.printStackTrace
    }
  }

  def createSubChildren(childrenMap: java.util.LinkedHashMap[String, Child], subChildRule: Rule, isAggregated: Boolean, isFixed: scala.Boolean, reserve: java.lang.String, child: Child) {
    var childName = subChildRule.getName()
    var subChild = new Child(childName)
    subChild.setAggregated(isAggregated)
    subChild.setFixed(isFixed)
    subChild.setReserve(reserve)
    subChild.setRule(subChildRule)
    subChild.setUserData(child)
    childrenMap.put(childName, subChild)
  }

  // --创建模型超类
  def createSuperEClass(rule: Rule, ownerEClass: EClass,
                        parentEClass: EClass, aggregated: java.lang.Boolean) {
    // List superTypes = parentEClass.getESuperTypes();
    var superName = parentEClass.getName + "-Element"
    var superEClass = superEClasses.get(new DoradoRuleMultiKey(superName, rule))
    var existed = false
    if (superEClass == null) {
      superEClass = simpleEClasses.get(new DoradoRuleMultiKey(superName, rule))
      if (superEClass == null) {
        superEClass = complexEClasses.get(new DoradoRuleMultiKey(superName, rule))

        if (superEClass == null) {
          superEClass = ecoreFactory.createEClass
          superEClass.setName(superName)
          superEClass.setAbstract(true)
          superEClasses.put(new DoradoRuleMultiKey(superName,
            rule), superEClass)
          createReference(parentEClass, superEClass, superName,
            aggregated)
        } else {
          existed = true
        }
      } else {
        existed = true
      }
    }

    if (existed)
      throw new RuntimeException("the super class[" + superName
        + "] don't is a concretely node")
    ownerEClass.getESuperTypes.add(superEClass)
  }

  def getOwnerEClass(name: String, ownName: String, childRule: Rule, complexEClass: EClass): org.eclipse.emf.ecore.EClass = {
    if (name.equals(ownName)) {
      return complexEClass
    } else {
      var ownerEClass = getEClassByNameAndRule(ownName, childRule)
      if (ownerEClass == null) {
        // --迭代
        var complexOwnerEClass = createComplexEClass(complexRules, childRule,
          ownName)
        complexEClasses.put(new DoradoRuleMultiKey(ownName,
          childRule), complexOwnerEClass)
        return complexOwnerEClass
      }
      return ownerEClass
    }
  }

  // --复杂类型模型规则
  /**
   * <Property>
   * |_<Entity>
   *  |_<Property>
   *    |_<Entity>
   *
   * |_<Collection>
   *  |_<Entity>
   *    |_<Property/>
   *
   */
  def createEntityEClasses() = {
    val entityElements: List[String] = List("Entity", "Collection")
    for (entityEClassName <- entityElements) {
      val entityEClass = ecoreFactory.createEClass()
      entityEClass.setName(entityEClassName)
      createPropertyReference(entityEClass)
      entityEClasses.put(entityEClassName, entityEClass)
      // --对当前propertyEClass进行处理,增加Entity的包含引用
      if (entityEClassName.equals("Collection")) {
        setPropertyReference(propertyEClass, entityEClass, false)
        addEntityReference(entityEClass)
        addValueReference(entityEClass)
      } else {
        setPropertyReference(propertyEClass, entityEClass, true)
      }
    }
  }

  def addValueReference(entityEClass: EClass) {
    var reference = ecoreFactory.createEReference()
    reference.setName(valueEClass.getName())
    reference.setEType(valueEClass)
    reference.setContainment(true)
    reference.setUpperBound(-1)
    entityEClass.getEStructuralFeatures().add(reference)
  }

  def setPropertyReference(propertyEClass: EClass,
                           entityEClass: EClass, aggregated: Boolean) {
    var reference = ecoreFactory.createEReference()
    reference.setName(entityEClass.getName())
    reference.setEType(entityEClass)
    reference.setContainment(true)
    if (aggregated)
      reference.setUpperBound(-1)
    else
      reference.setUpperBound(1)
    propertyEClass.getEStructuralFeatures().add(reference)
  }

  def addEntityReference(entityEClass: EClass) {
    var reference = ecoreFactory.createEReference()
    var eClass = entityEClasses.get("Entity")
    reference.setName(eClass.getName())
    reference.setEType(eClass)
    reference.setContainment(true)
    reference.setUpperBound(-1)
    entityEClass.getEStructuralFeatures().add(reference)
  }

  def createValueEClasses(): EClass = {
    valueEClass = ecoreFactory.createEClass()
    valueEClass.setName("Value")
    createValueEAttribute(valueEClass)
    return valueEClass
  }

  def createValueEAttribute(eClass: EClass) {
    var valueAttribute = ecoreFactory.createEAttribute()
    valueAttribute.setName("value")
    valueAttribute.setEType(ecorePackage.getEString())
    eClass.getEStructuralFeatures().add(valueAttribute)
  }

  def createPropertyEClass(): EClass = {
    propertyEClass = ecoreFactory.createEClass()
    propertyEClass.setName("Property")
    createPropertyEAttribute(propertyEClass)
    // --新增property对象节点
    createPropertyReference(propertyEClass)
    return propertyEClass
  }

  def createPropertyEAttribute(eClass: EClass) = {
    val valueAttribute = ecoreFactory.createEAttribute()
    valueAttribute.setName("value")
    valueAttribute.setEType(ecorePackage.getEString())
    eClass.getEStructuralFeatures().add(valueAttribute)
    val nameAttribute = ecoreFactory.createEAttribute()
    nameAttribute.setName("name")
    nameAttribute.setEType(ecorePackage.getEString())
    eClass.getEStructuralFeatures().add(nameAttribute)
  }

  def createPropertyReference(eClass: EClass) = {
    var reference = ecoreFactory.createEReference()
    reference.setName("Property")
    reference.setEType(propertyEClass)
    reference.setContainment(true)
    reference.setUpperBound(-1)
    var features = eClass.getEStructuralFeatures()
    features.add(reference)
  }

  def createClientEventEClass(): EClass = {
    clientEventEClass = ecoreFactory.createEClass()
    clientEventEClass.setName("ClientEvent")
    createClientEventEAttribute(clientEventEClass);
    return clientEventEClass
  }

  def createClientEventEAttribute(eClass: EClass) {
    var attribute = ecoreFactory.createEAttribute();
    attribute.setName("content");
    attribute.setEType(ecorePackage.getEString());
    eClass.getEStructuralFeatures().add(attribute);

    attribute = ecoreFactory.createEAttribute();
    attribute.setName("name");
    attribute.setEType(ecorePackage.getEString());
    eClass.getEStructuralFeatures().add(attribute);

    attribute = ecoreFactory.createEAttribute();
    attribute.setName("signature");
    attribute.setEType(ecorePackage.getEString());
    attribute.setDefaultValue("self,arg");

    eClass.getEStructuralFeatures().add(attribute);
  }

  def createAuxiliaryEClasses() {
    auxiliaryRule = complexRules.get(AUXILIARY_NAME)
    auxiliaryRule.getChildren().foreach(kv => createAuxiliaryEClass(kv._2))

  }

  def createAuxiliaryEClass(child: Child) = {
    var auxiliaryEClass = ecoreFactory.createEClass()
    var rule = child.getRule
    var className = rule.getName()
    auxiliaryEClass.setName(className)
    auxiliaryEClasses.put(className, auxiliaryEClass)
    registerModel(auxiliaryEClass, rule)
    addAttributes(auxiliaryEClass, rule)

  }

  def registerModel(eClass: EClass, rule: Rule) = {
    modelRegister.put(eClass, new DoradoConfigRulesModelMeta(project, rule))
  }

  def addAttributes(eClass: EClass, rule: Rule) = {
    val primitiveProps = rule.getPrimitiveProperties
    val iter = primitiveProps.values.iterator
    val attributes: java.util.List[String] = new java.util.ArrayList()
    while (iter.hasNext) {
      val property = iter.next()
      val name = property.getName()
      val propertyType = property.getType()
      val defaultValue = if (property.isFixed()) "" else getDefaultValue(property, rule)
      addAttribute(eClass, name, propertyType, attributes, defaultValue)
      addMetaOfAttribute(eClass.getEStructuralFeature(name),
        new DoradoAttributeMeta(property))
      val parentRule = getRuleByName("Container")
      if (rule.isSubRuleOf(parentRule) || rule.getName().equals("Container")
        || rule.getName().equals("AutoForm")) {
        addLayoutAttributes(eClass, rule, attributes)
      }
    }
    addLayoutConstraintAttributes(eClass, rule, attributes)
    addGraphicsAttributes(eClass, rule, attributes)
    addClientTypeAttributes(eClass, rule, attributes);
    clonePropertyToAttribute(eClass, rule, attributes)
  }

  // --平台定义规则：meta="clientType:desktop|touch""
  // --平台属性属性描述
  def addClientTypeAttributes(eClass: EClass, rule: Rule,
                              attributes: java.util.List[String]) {
    if ("ViewConfig".equals(rule.getName())) {
      createEAttribute(eClass, "meta", attributes, "")
    }
  }

  def addLayoutAttributes(eClass: EClass, rule: Rule,
                          attributes: java.util.List[String]) {
    addLayoutAttribute(eClass, "layout", getEcorePackage().getEString(),
      attributes)
  }

  def addLayoutConstraintAttributes(eClass: EClass, rule: Rule,
                                    attributes: java.util.List[String]) {
    var parentRule = getRuleByName("Conrol")
    if ((!eClass.getName().equals("View") && rule
      .isSubRuleOf(parentRule)) || eClass.getName().equals("Control")) {
      var dataType = ecorePackage.getEString
      createEAttribute(eClass, "layoutConstraint", attributes, "")
    }
  }

  def addLayoutAttribute(eClass: EClass, name: String,
                         eDataType: EDataType, attributes: java.util.List[String]) {
    if (eClass.getName().equals("AutoForm"))
      createEAttribute(eClass, name, attributes, "form")
    else
      createEAttribute(eClass, name, attributes, "dock")
  }

  // --图形信息属性定义规则：meta="color:${};x:${};y:${};collapse:${};child:${}"
  // --图形信息属性描述
  def addGraphicsAttributes(eClass: EClass, rule: Rule,
                            attributes: java.util.List[String]) {
    if (rule.isSubRuleOf(getRuleByName("PropertyDefSupport"))) {
      createEAttribute(eClass, "meta", attributes, "")
      return;
    }
    val abstractElements = scala.collection.immutable.List("AbstractDataType", "AbstractDataProvider", "AbstractDataResolver")
    for (abstractElement <- abstractElements) {
      var parentRule = getRuleByName(abstractElement)
      if (rule.isSubRuleOf(parentRule)) {
        createEAttribute(eClass, "meta", attributes, "")
        return
      }
    }
  }

  def addAuxiliaryReference(rule: Rule, className: String,
                            eClass: EClass) {
    // --守护DbModel中columns节点是否添加占位符对象
    if (className.equals("Columns")) {
      var childMap = rule.getChildren()
      var iter = childMap.values.iterator
      val tableColumnElements = Set("TableColumn", "TableKeyColumn", "SqlTableColumn", "AutoTableColumn")
      while (iter.hasNext) {
        var child = iter.next
        var childRule = child.getRule()
        if (tableColumnElements.contains(childRule.getName))
          return
      }
    }

    if (!noAuxiliaryRootElements.contains(className)) {
      addAuxiliaryReference(rule, eClass)
    }
  }

  def addAuxiliaryReference(rule: Rule, eClass: EClass) {
    auxiliaryEClasses.foreach(kv => createSuperEClass(rule, kv._2, eClass, new java.lang.Boolean(true)))
  }

  def clonePropertyToAttribute(eClass: EClass, rule: Rule,
                               attributes: java.util.List[String]) {
    var properties = rule.getProperties()
    properties.foreach(kv => clonePropertyToAttribute(kv._2, eClass, rule, attributes))
  }

  def clonePropertyToAttribute(prop: Property, eClass: EClass, rule: Rule, attributes: java.util.List[String]) {
    var propName = prop.getName()
    if (propName.equals("layout")) {
      var parentRule = getRuleByName("Container")
      if (rule.isSubRuleOf(parentRule)
        || eClass.getName().equals("Container")
        || eClass.getName().equals("AutoForm")) {
        addLayoutAttributes(eClass, rule, attributes)
      }
    } else {
      addAttribute(eClass, prop.getName(), prop.getType(),
        attributes, getDefaultValue(prop, rule))
      // --将Attribute附加信息附着在其模型的附加信息上
      addMetaOfAttribute(eClass.getEStructuralFeature(propName),
        new DoradoAttributeMeta(prop))
    }
  }

  // --将Attribute附加信息附着在其模型的附加信息上
  def addMetaOfAttribute(attribute: EStructuralFeature,
                         meta: DoradoAttributeMeta) {
    var entry = modelRegister.get(attribute.eContainer())
    entry.addAttributeMeta(meta)
  }

  def addAttribute(eClass: EClass, name: String, propertyType: String,
                   attributes: java.util.List[String], defaultValue: java.lang.Object) {
    createEAttribute(eClass, name, attributes,
      defaultValue)
  }

  def getDefaultValue(prop: Property, rule: Rule) = {
    if (org.apache.commons.lang.StringUtils.contains(rule.getName(),
      "Validator")) ""
    else prop.getDefaultValue()
  }

  def complete(eFactory: EcoreFactory) {
    val classifiers = ePackage.getEClassifiers()
    classifiers.add(propertyEClass)
    classifiers.add(clientEventEClass)
    classifiers.add(valueEClass)
    addClassifier(classifiers, entityEClasses)
    addClassifier(classifiers, auxiliaryEClasses)
    addClassifier(classifiers, simpleEClasses)
    addClassifier(classifiers, complexEClasses)
    System.out.println("success")
  }

  def addClassifier(classifiers: org.eclipse.emf.common.util.EList[org.eclipse.emf.ecore.EClassifier], eClasses: java.util.Map[_ <: Any, EClass]) {
    try {
      eClasses.foreach(kv => classifiers.add(kv._2))
    } catch {
      case ex: Exception => ex.printStackTrace()
    }
  }

  def createSimpleEClasses(simpleRules: java.util.Map[String, com.bstek.dorado.idesupport.model.Rule]) {
    simpleRules.foreach(kv => if (getEClassByNameAndRule(kv._2.getName, kv._2) == null) createSimpleEClass(kv._2))
  }

  def createSimpleEClass(simpleRule: Rule):org.eclipse.emf.ecore.EClass={
    val className = simpleRule.getName()
    val simpleEClass = ecoreFactory.createEClass()
    simpleEClass.setName(className)
    simpleEClasses.put(
      new DoradoRuleMultiKey(className, simpleRule), simpleEClass)
    //--注册模型信息
    registerModel(simpleEClass, simpleRule)
    // --属性定义
    addAttributes(simpleEClass, simpleRule)
    val clientEventMap = simpleRule.getClientEvents()
    if (!clientEventMap.isEmpty()) {
      addClientEventReference(simpleEClass, simpleRule)
    }
    // --Property引用定义
    addPropertyReference(simpleEClass)
    addAuxiliaryReference(simpleRule, className, simpleEClass)
    return simpleEClass
  }

  def addClientEventReference(eClass: EClass, rule: Rule) = {
    var reference = ecoreFactory.createEReference()
    reference.setName("ClientEvent")
    reference.setEType(clientEventEClass)
    reference.setContainment(true)
    reference.setUpperBound(-1)
    var features = eClass.getEStructuralFeatures
    features.add(reference)
  }

  def addPropertyReference(eClass: EClass) {
    createPropertyReference(eClass)
  }

  def getPropertyEClass() = propertyEClass

  def getEPackage() = ePackage

  def getClientEventEClass() = clientEventEClass

  def createEAttribute(eClass: EClass, name: String, attributes: java.util.List[String], defaultValue: java.lang.Object) = {
    val attribute = ecoreFactory.createEAttribute()
    attribute.setName(name)
    attribute.setEType(eDataType)

    if (defaultValue != null) {
      if (eClass.getName().equals("DataType")
        && name.equals("parent")) {
        attribute.setDefaultValue("")
      } else {
        attribute.setDefaultValue(defaultValue)
      }
    } else {
      attribute.setDefaultValue("")
    }
    if (!attributes.contains(name)) {
      var features: org.eclipse.emf.common.util.EList[org.eclipse.emf.ecore.EStructuralFeature] = eClass.getEStructuralFeatures()
      features.add(attribute)
      attributes.add(name)
    }
  }

  def getMetaOfModel(eClass: EClass): DoradoConfigRulesModelMeta = {
    modelRegister.get(eClass)
  }

  def getMetaOfModel(model: EObject): DoradoConfigRulesModelMeta = {
    if (model != null) getMetaOfModel(model.eClass) else null

  }

  def getMetaOfReference(eClass: EClass, reference: EObject): DoradoReferenceMeta = {
    val modelMeta = modelRegister.get(eClass)
    if (modelMeta != null) modelMeta.getReferenceMeta(reference.eClass().getName()) else null
  }

  def getMetaOfAttribute(attribute: EAttribute): DoradoAttributeMeta = {
    var modelMeta = modelRegister.get(attribute.eContainer())
    if (modelMeta != null) modelMeta.getAttributeMeta(attribute.getName()) else null
  }

  def getRuleSet(project: Project, refresh: Boolean): RuleSet = {
    if (!refresh && ruleSet != null) ruleSet else getRuleSet(project)
  }

  def getRuleSet(project: Project): RuleSet = {
    DoradoRuleSetConfig.createRuleSet(project)
  }

  def getRuleByName(name: String): Rule = {
    val rule = simpleRules.get(name)
    if (rule == null) complexRules.get(name) else rule
  }

  def getEClassByNameAndRule(className: String,
                             rule: Rule): EClass = {
    var multiKey = new DoradoRuleMultiKey(className, rule)
    if (simpleEClasses.containsKey(multiKey)) {
      return simpleEClasses.get(multiKey)
    }
    if (complexEClasses.containsKey(multiKey)) {
      return complexEClasses.get(multiKey)
    }
    if (auxiliaryEClasses.containsKey(className)) {
      return auxiliaryEClasses.get(className)
    }
    null
  }

  def createReference(parentEClass: EClass, eClasss: EClass,
                      referenceName: String, aggregated: java.lang.Boolean) {
    var reference = ecoreFactory.createEReference()
    parentEClass.getEStructuralFeatures().add(reference)
    reference.setEType(eClasss)
    reference.setContainment(true)
    reference.setName(referenceName)
    if (aggregated.booleanValue())
      reference.setUpperBound(-1)
    else
      reference.setUpperBound(1)

  }

  // --将Reference(引用)附加信息附着在其模型的eClass对象上
  def addMetaOfReference(eClass: EClass, meta: DoradoReferenceMeta) {
    var entry = modelRegister
      .get(eClass)
    if (entry != null)
      entry.addReferenceMeta(meta)
  }

}