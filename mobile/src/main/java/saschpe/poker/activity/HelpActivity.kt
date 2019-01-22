/*
 * Copyright 2016 Sascha Peilicke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package saschpe.poker.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_help.*
import saschpe.android.socialfragment.app.SocialFragment
import saschpe.android.versioninfo.widget.VersionInfoDialogFragment
import saschpe.poker.BuildConfig
import saschpe.poker.R
import saschpe.poker.application.Application
import saschpe.poker.extensions.startPrivacyPolicy

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        if (intent != null) {
            if (intent.scheme != null && intent.scheme == Application.INTENT_SCHEME) {
                val uri = intent.data
                if (uri != null && uri.host != null && uri.host == "about") {
                    when (uri.path) {
                        "/privacy" -> {
                            startPrivacyPolicy()
                            finish()
                        }
                    }
                }
            }
        }

        setContentView(R.layout.activity_help)

        // Set up toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up nested scrollview
        nested_scroll.isFillViewport = true

        // Set up view pager
        view_pager.adapter = MyFragmentPagerAdapter(this, supportFragmentManager)

        // Set up  tab layout
        tab_layout.tabMode = TabLayout.MODE_FIXED
        tab_layout.setupWithViewPager(view_pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.privacy_policy -> startPrivacyPolicy()
            R.id.open_source_licenses -> startActivity(Intent(this, OssLicensesMenuActivity::class.java))
            R.id.version_info -> {
                VersionInfoDialogFragment
                    .newInstance(
                        getString(R.string.app_name),
                        BuildConfig.VERSION_NAME,
                        "Sascha Peilicke",
                        R.mipmap.ic_launcher
                    )
                    .show(supportFragmentManager, "version_info")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private class MyFragmentPagerAdapter(context: Context, fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val pageTitles = arrayOf(context.getString(R.string.social))
        private val applicationName = context.getString(R.string.app_name)

        override fun getItem(position: Int): Fragment = SocialFragment.Builder()
            // Mandatory
            .setApplicationId(BuildConfig.APPLICATION_ID)
            // Optional
            .setApplicationName(applicationName)
            .setContactEmailAddress("sascha+gp@peilicke.de")
            .setGithubProject("saschpe/PlanningPoker")
            .setTwitterProfile("saschpe")
            // Visual customization
            .setHeaderTextColor(R.color.accent)
            .setIconTint(android.R.color.white)
            .build()

        override fun getPageTitle(position: Int): String = pageTitles[position]

        override fun getCount() = 1
    }
}
