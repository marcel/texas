package com.andbutso.poker

import com.andbutso.poker.fixtures.AllFixtures
import com.andbutso.poker.handhistory.ChipAmount
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito

trait ParentSpec extends SpecificationWithJUnit with AllFixtures with Mockito {
  args.execute(isolated = true, sequential = true)

  def $(string: String) = ChipAmount.fromString(string)

  def identityPF[A]: PartialFunction[A, A] = { case x => x }
}