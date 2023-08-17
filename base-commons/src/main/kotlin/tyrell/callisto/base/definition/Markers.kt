package tyrell.callisto.base.definition

/**
 * Marker interface [TransferredModel] outlines classes that can be transferred via API in serialized representation.
 * Classes marked with [TransferredModel] are expected be serialized into JSON or/and XML (depending on concrete domain).
 *
* Class **MUST** be inherited from [TransferredModel] annotation
 * if its instances are serialized (deserialized) for being supplied (received) through API (e.g. through REST API)
 *
 * Developer **SHOULD** inherit class from [TransferredModel] annotation
 * if class instances are supposed to be serialized (deserialized)
 * for being supplied (received) through API (e.g. through REST API)
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface TransferredModel

/**
 * Marker interface [SerializedAttributes] outlines classes
 * that represent nested attributes container (e.g. DB table entity)
 *
 * Example usage of [SerializedAttributes] marker interface
 * is marking class that represents contents of unstructured DB column within [JPA entity](https://docs.oracle.com/cd/E16439_01/doc.1013/e13981/undejbs003.htm).
 *
 * Class **MUST** be marked with [SerializedAttributes] marker interface
 * if it represents contents of unstructured DB column.
 *
 * Property **SHOULD NOT** be added to class marked with [SerializedAttributes] marker interface
 * if the class is used by JPA entity and the property is expected to be filtered using [SQL](https://en.wikipedia.org/wiki/SQL).
 *
 * Property **MUST NOT** be added to class marked with [SerializedAttributes] marker interface
 * if class represents contents of unstructured DB column
 * and values of the property refer to other DB table column (property values has `FOREIGN KEY` semantics).
 *
 * @author Mikhail Gostev
 * @since 0.0.1
 */
@LibraryApi
public interface SerializedAttributes
