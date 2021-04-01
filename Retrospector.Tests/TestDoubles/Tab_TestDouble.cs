using System.Diagnostics.CodeAnalysis;
using Retrospector.MediaTab.Interfaces;
using Retrospector.SearchTab.Interfaces;

namespace Retrospector.Tests.TestDoubles
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class Tab_TestDouble : ISearchTab, IMediaTab
    {

    }
}