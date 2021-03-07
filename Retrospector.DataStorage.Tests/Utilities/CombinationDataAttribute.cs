using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Xunit;
using Xunit.Sdk;

namespace Retrospector.DataStorage.Tests.Utilities
{
    [AttributeUsage(AttributeTargets.Method, AllowMultiple = true)]
    public class CombinationDataAttribute : DataAttribute
    {
        private readonly string[] _memberDatas;

        public CombinationDataAttribute(params string[] memberDatas)
        {
            _memberDatas = memberDatas;
        }

        public override IEnumerable<object[]> GetData(MethodInfo testMethod)
        {
            var dataSources = _memberDatas
                .Select(memberData => LoadMemberData(memberData, testMethod))
                .ToArray();
            return Combinator.AllCombinationsOf(dataSources);
        }

        private IEnumerable<object[]> LoadMemberData(string memberData, MethodInfo testMethod)
        {
            return new MemberDataAttribute(memberData).GetData(testMethod).ToList();
        }
    }
}