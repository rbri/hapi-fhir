package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Encounter;
import org.hl7.fhir.r4b.model.Resource;
import org.hl7.fhir.r5.model.SubscriptionTopic;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubscriptionTopicPayloadBuilderR4BTest {
	FhirContext ourFhirContext = FhirContext.forR4BCached();
	@Test
	public void testBuildPayloadDelete() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");
		SubscriptionTopic topic = new SubscriptionTopic();
		msg.setOperationType(BaseResourceMessage.OperationTypeEnum.DELETE);

		// run
		Bundle payload = (Bundle)svc.buildPayload(encounter, msg, subscription, topic);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(1, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.DELETE, payload.getEntry().get(1).getRequest().getMethod());
	}

	@Test
	public void testBuildPayloadUpdate() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");
		SubscriptionTopic topic = new SubscriptionTopic();
		msg.setOperationType(BaseResourceMessage.OperationTypeEnum.UPDATE);

		// run
		Bundle payload = (Bundle)svc.buildPayload(encounter, msg, subscription, topic);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(2, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());
		assertEquals("Encounter", resources.get(1).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.PUT, payload.getEntry().get(1).getRequest().getMethod());
	}

	@Test
	public void testBuildPayloadCreate() {
		// setup
		var svc = new SubscriptionTopicPayloadBuilder(ourFhirContext);
		var encounter = new Encounter();
		encounter.setId("Encounter/1");
		ResourceModifiedMessage msg = new ResourceModifiedMessage();
		CanonicalSubscription sub = new CanonicalSubscription();
		ActiveSubscription subscription = new ActiveSubscription(sub, "test");
		SubscriptionTopic topic = new SubscriptionTopic();
		msg.setOperationType(BaseResourceMessage.OperationTypeEnum.CREATE);

		// run
		Bundle payload = (Bundle)svc.buildPayload(encounter, msg, subscription, topic);

		// verify
		List<Resource> resources = BundleUtil.toListOfResourcesOfType(ourFhirContext, payload, Resource.class);
		assertEquals(2, resources.size());
		assertEquals("SubscriptionStatus", resources.get(0).getResourceType().name());
		assertEquals("Encounter", resources.get(1).getResourceType().name());

		assertEquals(Bundle.HTTPVerb.POST, payload.getEntry().get(1).getRequest().getMethod());
	}
}
