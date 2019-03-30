package com.walmart.logistics.rlog.bridge.test.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;

import com.walmart.logistics.rlog.bridge.test.constants.ESBTestConstant;
import com.walmart.logistics.rlog.bridge.test.util.ESBTestUtil;
import com.walmart.logistics.rlog.bridge.test.vo.RouteTestVO;




/**
 * AbstractBaseTest is a base class implementation for testing schedulers
 * implementation
 * 
 * @author Cognizant
 * 
 */
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public abstract class AbstractBaseTest {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractBaseTest.class);
	private static final int MESSAGE_COUNT = 1;



	@Inject
	private ModelCamelContext errorhandlingContext;

	

	protected void testApplRoutes(final ModelCamelContext camelContext,
			final ProducerTemplate producerTemplate, final RouteTestVO testVo,
			final boolean isErrorHandle) {
		try {
			if (!isErrorHandle) {
				handleApplRoutes(camelContext, testVo);
			} else {
				moveErrorLoc(testVo, camelContext);
			}
			/*if (isErrorHandle) {
				handleErrorEndPoint(testVo);
			}*/

			producerTemplate.sendBody(testVo.getMockFromURI(),
					testVo.getMessage());

			assertNotNull(camelContext.hasEndpoint(testVo.getFromURI()));
			assertNotNull(camelContext.hasEndpoint(testVo.getMockFromURI()));
			
			
			assertNotNull(camelContext.hasEndpoint(testVo.getToURI()));

			if (isErrorHandle) {
				assertNotNull(camelContext
						.hasEndpoint(ESBTestConstant.ERROR_FROM_URI));
			} else {
				assertNotNull(camelContext.hasEndpoint(testVo.getMockToURI()));
				
			}

			MockEndpoint.assertIsSatisfied(camelContext);
		} catch (Exception e) {
			LOG.error("error in testApplRoutes", e);
		}
	}

/*	private void handleErrorEndPoint(final RouteTestVO testVo) {
		try {
			assertEquals(ESBTestConstant.ERROR_HANDLE_CONTEXT,
					errorhandlingContext.getName());
			final RouteDefinition ERRORouteDef = errorhandlingContext
					.getRouteDefinition(ESBTestConstant.ERROR_HANDLE_ROUTE);
			assertNotNull(ERRORouteDef);
			String mockTestURI = "mock://esb-test-to";
			final boolean hasTestEP = ESBTestUtil.hasEndPoint(
					errorhandlingContext, mockTestURI);

			errorhandlingContext.getRouteDefinition(
					ESBTestConstant.ERROR_HANDLE_ROUTE).adviceWith(
					errorhandlingContext, new AdviceWithRouteBuilder() {
						@Override
						public void configure() {
							final MockEndpoint resultEndpoint = errorhandlingContext
									.getEndpoint(testVo.getMockToURI(),
											MockEndpoint.class);
							resultEndpoint.expectedBodiesReceived(testVo
									.getMessage());

							resultEndpoint.expectedMessageCount(MESSAGE_COUNT);
							if (!hasTestEP) {
								weaveById(ESBTestConstant.ERROR_HANDLE_PRO_ID)
										.replace().to(resultEndpoint);

							}

						}
					});
		} catch (Exception e) {
			LOG.error("error in handleErrorEndPoint ", e);
		}

	}*/

	private void handleApplRoutes(final ModelCamelContext camelContext,
			final RouteTestVO testVo) {
		assertEquals(testVo.getContextName(), camelContext.getName());
		final RouteDefinition routeDef = camelContext.getRouteDefinition(testVo
				.getRouteId());
		assertNotNull(routeDef);
		String feedName = ESBTestUtil.getHeaderValueFromRoute(routeDef,
				ESBTestConstant.FEED_NAME);
		assertEquals(testVo.getFeedName(), feedName);

		LOG.debug(" Unit Test Context=" + testVo.getContextName()
				+ " Route Id=" + testVo.getRouteId() + " FromUuri="
				+ testVo.getFromURI() + " ToUri=" + testVo.getToURI());

		try {
			routeDef.adviceWith(camelContext, new AdviceWithRouteBuilder() {
				@Override
				public void configure() {

					replaceFromWith(testVo.getMockFromURI());
					MockEndpoint resultEndpoint = getMockEndpoint(testVo,
							camelContext);
					resultEndpoint.expectedBodiesReceived(testVo.getMessage());
					resultEndpoint.expectedMessageCount(MESSAGE_COUNT);
				/*	if (testVo.getMqFileStorePath() != null) {
						interceptSendToEndpoint(testVo.getMqFileStorePath())
								.skipSendToOriginalEndpoint().to(
										testVo.getMockMqFileStorePath());
					}*/
					//replaceMockEndpoint(testVo, camelContext);
				}
			});
		} catch (Exception e) {
			LOG.error("error in handleErrorEndPoint ", e);
		}
	}

	private void moveErrorLoc(final RouteTestVO testVo,
			final ModelCamelContext camelContext) {

		try {
			final RouteDefinition routeDef = camelContext
					.getRouteDefinition(testVo.getRouteId());
			routeDef.adviceWith(camelContext, new AdviceWithRouteBuilder() {
				@Override
				public void configure() {
					interceptSendToEndpoint(testVo.getFromURI())
							.skipSendToOriginalEndpoint().to(
									ESBTestConstant.ERROR_FROM_URI);
				}
			});
		} catch (Exception e) {
			LOG.error("error in handleErrorEndPoint ", e);
		}
	}
/*
	private void replaceMockEndpoint(final RouteTestVO testVo,
			final ModelCamelContext camelContext) {
		try {
			final RouteDefinition routeDef = camelContext
					.getRouteDefinition(testVo.getRouteId());
			routeDef.adviceWith(camelContext, new AdviceWithRouteBuilder() {
				@Override
				public void configure() {
					MockEndpoint resultEndpoint = getMockEndpoint(testVo,
							camelContext);
					weaveById(testVo.getBeanReqProId()).replace().to(
							testVo.getMockBeanReqProURI());

					interceptSendToEndpoint(testVo.getToURI())
							.skipSendToOriginalEndpoint().to(
									testVo.getMockToURI());

					weaveById(testVo.getBeanUpdateProId()).replace().to(
							resultEndpoint);
				}
			});
		} catch (Exception e) {
			LOG.error("error in handleErrorEndPoint ", e);
		}

	}*/

	private MockEndpoint getMockEndpoint(final RouteTestVO testVo,
			final ModelCamelContext camelContext) {
		MockEndpoint resultEndpoint = null;
		resultEndpoint = camelContext.getEndpoint(
				testVo.getMockToURI(), MockEndpoint.class);
		resultEndpoint.expectedBodiesReceived(testVo.getMessage());
		return resultEndpoint;
	}

}
