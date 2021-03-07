using System.Diagnostics.CodeAnalysis;
using Retrospector.Core.Crud.Models;
using Retrospector.DataStorage.Medias.Entities;
using Retrospector.DataStorage.Medias.Interfaces;
using Retrospector.DataStorage.Tests.Utilities;

namespace Retrospector.DataStorage.Tests.TestDoubles.Medias
{
    [SuppressMessage("ReSharper", "InconsistentNaming")]
    public class MediaTypeMapper_TestDouble : Mapper_TestDouble<MediaType, MediaTypeEntity>, IMediaTypeMapper
    { }
}