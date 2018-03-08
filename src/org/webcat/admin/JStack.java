/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Licensed to the Apache Software Foundation (ASF) under one
 |  or more contributor license agreements.  See the NOTICE file
 |  distributed with this work for additional information
 |  regarding copyright ownership.  The ASF licenses this file
 |  to you under the Apache License, Version 2.0 (the
 |  "License"); you may not use this file except in compliance
 |  with the License.  You may obtain a copy of the License at
 |
 |     http://www.apache.org/licenses/LICENSE-2.0
 |
 |  Unless required by applicable law or agreed to in writing, software
 |  distributed under the License is distributed on an "AS IS" BASIS,
 |  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 |  See the License for the specific language governing permissions and
 |  limitations under the License.
\*==========================================================================*/


package org.webcat.admin;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import org.apache.log4j.Logger;
import org.webcat.core.Application;

//-------------------------------------------------------------------------
/**
 * Print all of the information about all the current threads, similar
 * to the jstack command.
 *
 * @author  edwards
 * @author  Last changed by $Author$
 * @version $Revision$, $Date$
 */
public class JStack
{
    //~ Instance/static fields ................................................

    static private ThreadMXBean threadBean =
        ManagementFactory.getThreadMXBean();
    static Logger log = Logger.getLogger(JStack.class);

    //~ Methods ...............................................................

      public static void setContentionTracing(boolean val)
      {
          threadBean.setThreadContentionMonitoringEnabled(val);
      }

      private static String getTaskName(long id, String name)
      {
          if (name == null)
          {
              return Long.toString(id);
          }
          return id + " (" + name + ")";
      }

      // ----------------------------------------------------------
      /**
       * Triggers a JStack-like thread dump.
       */
      public static void threadDump()
      {
          printThreadInfo(Application.configurationProperties()
              .getProperty("grader.submissiondir") + "/thread-dump."
              + System.currentTimeMillis() + ".txt");
      }


      public static void printThreadInfo(String fileName)
      {
          try
          {
              printThreadInfo(new PrintWriter(fileName));
          }
          catch (FileNotFoundException e)
          {
              log.error(e);
          }
      }

      /**
       * Print all of the thread's information and stack traces.
       *
       * @param stream
       *          the stream to
       * @param title
       *          a string title for the stack trace
       */
      public static void printThreadInfo(PrintWriter stream)
      {
          final int STACK_DEPTH = 20;
          boolean contention = threadBean.isThreadContentionMonitoringEnabled();
          long[] threadIds = threadBean.getAllThreadIds();
          stream.println("Process Thread Dump:");
          stream.println(threadIds.length + " active threads");
          for (long tid : threadIds)
          {
              ThreadInfo info = threadBean.getThreadInfo(tid, STACK_DEPTH);
              if (info == null)
              {
                  stream.println("  Inactive");
                  continue;
              }
              stream.println("Thread "
                  + getTaskName(info.getThreadId(), info.getThreadName())
                  + ":");
              Thread.State state = info.getThreadState();
              stream.println("  State: " + state);
              stream.println("  Blocked count: " + info.getBlockedCount());
              stream.println("  Waited count: " + info.getWaitedCount());
              if (contention)
              {
                  stream.println("  Blocked time: " + info.getBlockedTime());
                  stream.println("  Waited time: " + info.getWaitedTime());
              }
              if (state == Thread.State.WAITING)
              {
                  stream.println("  Waiting on " + info.getLockName());
              }
              else if (state == Thread.State.BLOCKED)
              {
                  stream.println("  Blocked on " + info.getLockName());
                  stream.println("  Blocked by "
                      + getTaskName(info.getLockOwnerId(),
                          info.getLockOwnerName()));
              }
              stream.println("  Stack:");
              for (StackTraceElement frame : info.getStackTrace())
              {
                  stream.println("    " + frame.toString());
              }
          }
          stream.flush();
      }
}
