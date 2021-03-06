/*
 *    Copyright 2018 APPNEXUS INC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.appnexus.opensdk;

import com.appnexus.opensdk.shadows.ShadowAsyncTaskNoExecutor;
import com.appnexus.opensdk.shadows.ShadowCustomWebView;
import com.appnexus.opensdk.shadows.ShadowSettings;
import com.appnexus.opensdk.shadows.ShadowWebSettings;
import com.appnexus.opensdk.ut.UTConstants;
import com.squareup.okhttp.mockwebserver.MockResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@Config(sdk = 21,
        shadows = {ShadowAsyncTaskNoExecutor.class,
                ShadowCustomWebView.class, ShadowWebSettings.class, ShadowSettings.class, ShadowLog.class})
@RunWith(RobolectricTestRunner.class)
public class ANAdResponseInfoBannerTests extends BaseViewAdTest {

    @Override
    public void setup() {
        super.setup();
    }

    @Override
    public void tearDown() {
        super.tearDown();
    }

    @Test
    public void testAdResponseInfoRTBBanner() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.banner())); // First queue a regular HTML banner response
        assertNull(bannerAdView.getAdResponseInfo());
        assertTrue(bannerAdView.getAdType() == AdType.UNKNOWN); // First tests if ad_type is UNKNOWN initially
        executeBannerRequest();
        assertTrue(bannerAdView.getAdType() == AdType.BANNER); // If a HTML banner is served then BANNER
        assertNotNull(bannerAdView.getAdResponseInfo());
        assertEquals(bannerAdView.getAdResponseInfo().getAdType(), AdType.BANNER);
        assertEquals(bannerAdView.getAdResponseInfo().getCreativeId(), "6332753");
        assertEquals(bannerAdView.getAdResponseInfo().getTagId(), "123456");
        assertEquals(bannerAdView.getAdResponseInfo().getBuyMemberId(), 123);
        assertEquals(bannerAdView.getAdResponseInfo().getContentSource(), UTConstants.RTB);
        assertEquals(bannerAdView.getAdResponseInfo().getNetworkName(), "");
    }

//    @Test
//    public void testAdResponseInfoCSMBanner() {
//        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.noFillCSMBanner())); // First queue a regular HTML banner response
//        assertNull(bannerAdView.getAdResponseInfo());
//        assertTrue(bannerAdView.getAdType() == AdType.UNKNOWN); // First tests if ad_type is UNKNOWN initially
//        executeBannerRequest();
//        assertTrue(bannerAdView.getAdType() == AdType.BANNER); // If a HTML banner is served then BANNER
//        assertNotNull(bannerAdView.getAdResponseInfo());
//        assertEquals(bannerAdView.getAdResponseInfo().getAdType(), AdType.BANNER);
//        assertEquals(bannerAdView.getAdResponseInfo().getCreativeId(), "44863345");
//        assertEquals(bannerAdView.getAdResponseInfo().getTagId(), "123456");
//        assertEquals(bannerAdView.getAdResponseInfo().getBuyMemberId(), 123);
//        assertEquals(bannerAdView.getAdResponseInfo().getContentSource(), UTConstants.CSM);
//        assertEquals(bannerAdView.getAdResponseInfo().getNetworkName(), "com.appnexus.opensdk.testviews.MediatedBannerNoFillView");
//    }

    @Test
    public void testAdResponseInfoRTBBannerNative() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.anNative())); // First queue a regular HTML banner response
        assertNull(bannerAdView.getAdResponseInfo());
        assertTrue(bannerAdView.getAdType() == AdType.UNKNOWN); // First tests if ad_type is UNKNOWN initially
        executeBannerRequest();
        assertTrue(bannerAdView.getAdType() == AdType.NATIVE); // If a HTML banner is served then BANNER
        assertNull(bannerAdView.getAdResponseInfo());

        assertNotNull(nativeAdResponse.getAdResponseInfo());
        assertEquals(nativeAdResponse.getAdResponseInfo().getAdType(), AdType.NATIVE);
        assertEquals(nativeAdResponse.getAdResponseInfo().getCreativeId(), "47772560");
        assertEquals(nativeAdResponse.getAdResponseInfo().getTagId(), "123456");
        assertEquals(nativeAdResponse.getAdResponseInfo().getBuyMemberId(), 958);
        assertEquals(nativeAdResponse.getAdResponseInfo().getContentSource(), UTConstants.RTB);
        assertEquals(nativeAdResponse.getAdResponseInfo().getNetworkName(), "");
    }

    public void testAdResponseInfoRTBBannerNativeRenderer() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.anNativeRenderer())); // First queue a regular HTML banner response
        bannerAdView.enableNativeRendering(true);
        assertNull(bannerAdView.getAdResponseInfo());
        assertTrue(bannerAdView.getAdType() == AdType.UNKNOWN); // First tests if ad_type is UNKNOWN initially
        executeBannerRequest();
        assertNotNull(bannerAdView.getAdResponseInfo());
        assertEquals(bannerAdView.getAdResponseInfo().getAdType(), AdType.NATIVE);
        assertEquals(bannerAdView.getAdResponseInfo().getCreativeId(), "47772560");
        assertEquals(bannerAdView.getAdResponseInfo().getTagId(), "123456");
        assertEquals(bannerAdView.getAdResponseInfo().getBuyMemberId(), 958);
        assertEquals(bannerAdView.getAdResponseInfo().getContentSource(), UTConstants.RTB);
        assertEquals(bannerAdView.getAdResponseInfo().getNetworkName(), "");
        assertNull(nativeAdResponse);
    }

    @Test
    public void testAdResponseInfoRTBBannerNoBid() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.NO_BID));
        assertNull(bannerAdView.getAdResponseInfo());
        executeBannerRequest();
        assertNotNull(bannerAdView.getAdResponseInfo());
        assertEquals(bannerAdView.getAdResponseInfo().getAdType(), null);
        assertEquals(bannerAdView.getAdResponseInfo().getCreativeId(), "");
        assertEquals(bannerAdView.getAdResponseInfo().getTagId(), "123456789");
        assertEquals(bannerAdView.getAdResponseInfo().getBuyMemberId(), 0);
        assertEquals(bannerAdView.getAdResponseInfo().getContentSource(), "");
        assertEquals(bannerAdView.getAdResponseInfo().getNetworkName(), "");
    }

    @Test
    public void testAdResponseInfoRTBBannerBlank() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.blank()));
        assertNull(bannerAdView.getAdResponseInfo());
        executeBannerRequest();
        assertNull(bannerAdView.getAdResponseInfo());
    }

    @Test
    public void testGetCreativeIdBanner() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.banner())); // First queue a regular HTML banner response
        assertNull(bannerAdView.getAdResponseInfo());
        executeBannerRequest();
        assertEquals("6332753", bannerAdView.getAdResponseInfo().getCreativeId());
    }

    @Test
    public void testGetCreativeIdBannerNativeCreativeId() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.anNative())); // First queue a banner Native response
        assertNull(bannerAdView.getAdResponseInfo());
        executeBannerRequest();
        assertEquals("47772560", nativeAdResponse.getAdResponseInfo().getCreativeId());
    }

    @Test
    public void testGetAdResponseInfoNullForBlankBannerResponse() {
        server.enqueue(new MockResponse().setResponseCode(200).setBody(TestResponsesUT.blankBanner())); // First queue a regular HTML banner response
        assertNull(bannerAdView.getAdResponseInfo());
        executeBannerRequest();
        assertNull(bannerAdView.getAdResponseInfo());
    }

    private void executeBannerRequest() {
        bannerAdView.setAutoRefreshInterval(15000);
        bannerAdView.loadAd();

        waitForTasks();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();

        waitForTasks();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();

//        Robolectric.getBackgroundThreadScheduler().advanceToNextPostedRunnable();
//        Robolectric.getForegroundThreadScheduler().advanceToNextPostedRunnable();
    }
}
